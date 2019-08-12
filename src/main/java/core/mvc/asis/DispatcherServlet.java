package core.mvc.asis;

import core.exceptions.HandlerMappingException;
import core.mvc.Handler;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.ModelAndViewHandler;
import core.mvc.tobe.AnnotationHandlerMapping;
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
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String BASE_PACKAGE = "next.controller";

    private List<HandlerMapping<? extends Handler>> handlerMappings = new ArrayList<>();


    @Override
    public void init() throws ServletException {
        LegacyHandlerMapping rm = new LegacyHandlerMapping();
        rm.initMapping();
        handlerMappings.add(rm);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Handler handler = handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new HandlerMappingException("not found handler mapping"));

        try {
            executeHandler(handler, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void executeHandler(Handler handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handler instanceof ModelAndViewHandler) {
            ModelAndView modelAndView = ((ModelAndViewHandler)handler).handle(req, resp);
            viewRender(modelAndView, req, resp);
        } else {
            throw new HandlerMappingException("not supported handler");
        }
    }

    private void viewRender(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (modelAndView == null) {
            return;
        }

        if (modelAndView.getView() == null) {
            return;
        }

        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }
}
