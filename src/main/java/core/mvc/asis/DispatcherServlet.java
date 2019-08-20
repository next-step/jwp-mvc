package core.mvc.asis;

import core.mvc.HandlerAdaptor;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.ExecutionHandlerAdaptor;
import core.mvc.tobe.HandlerExecution;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String DEFAULT_BASE_PACKAGE = "next.controller";

    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    @Override
    public void init() {
        initHandleMappings();
        initHandlerAdaptors();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = getHandler(req);
        HandlerAdaptor handlerAdaptor = getHandlerAdapter(handler);

        try {
            ModelAndView mav = handlerAdaptor.handle(req, resp, handler);

            View view = mav.getView();
            view.render(mav.getModel(), req, resp);

        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void initHandleMappings() {
        LegacyHandlerMapping legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.init();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(DEFAULT_BASE_PACKAGE);
        annotationHandlerMapping.init();

        handlerMappings.add(legacyHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);
    }

    private void initHandlerAdaptors() {
        handlerAdaptors.add(new LagacyHandlerAdaptor());
        handlerAdaptors.add(new ExecutionHandlerAdaptor());
    }

    private Object getHandler(HttpServletRequest req) throws ServletException {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(req))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(ServletException::new);
    }

    private HandlerAdaptor getHandlerAdapter(Object mapper) throws ServletException {
        return handlerAdaptors.stream()
            .filter(handlerAdaptor -> handlerAdaptor.isSupport(mapper))
            .findFirst()
            .orElseThrow(ServletException::new);
    }
}
