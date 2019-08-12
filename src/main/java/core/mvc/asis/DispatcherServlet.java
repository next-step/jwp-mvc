package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        handlerMappings.add(createLegacyHandlerMapping());
        handlerMappings.add(createAnnotationHandlerMapping());

        handlerAdapters.add(new LegacyHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            final Object handler = getHandler(req);
            final ModelAndView modelAndView = getModelAndView(req, resp, handler);
            render(req, resp, modelAndView);
        } catch (Throwable e) {
            logger.error("Exception : {}", e.getMessage());
            throw new ServletException(e);
        }
    }

    private LegacyHandlerMapping createLegacyHandlerMapping() {
        final LegacyHandlerMapping legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        return legacyHandlerMapping;
    }

    private AnnotationHandlerMapping createAnnotationHandlerMapping() throws ServletException {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        try {
            annotationHandlerMapping.initialize();
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException : {}", e.getMessage());
            throw new ServletException(e);
        }
        return annotationHandlerMapping;
    }

    private Object getHandler(HttpServletRequest req) throws ServletException {
        return handlerMappings.stream()
                .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(req)))
                .findFirst()
                .orElseThrow(() -> new ServletException("페이지를 찾을 수 없습니다."));
    }

    private ModelAndView getModelAndView(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        final HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new ServletException("페이지를 찾을 수 없습니다."));

        return handlerAdapter.handle(req, resp, handler);
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        final Map<String, Object> model = modelAndView.getModel();
        final View view = modelAndView.getView();

        view.render(model, req, resp);
    }
}
