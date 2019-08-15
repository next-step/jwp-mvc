package core.mvc.asis;

import core.annotation.web.RequestMethod;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.ModelAndViewWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String ANNOTATION_CONTROLLER_PACKAGE = "next.controller";

    private LegacyHandlerMapping legacyHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        annotationHandlerMapping = new AnnotationHandlerMapping(ANNOTATION_CONTROLLER_PACKAGE);
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        Object handler = getHandler(request);
        try {
            move(handler, request, response);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        if (annotationHandlerMapping.isHandlerKeyPresent(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))) {
            return annotationHandlerMapping.getHandler(request);
        }

        return legacyHandlerMapping.getHandler(request);
    }

    private void move(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = getModelAndView(handler, request, response);
        String viewName = modelAndView.getViewName();

        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        modelAndView.render(request, response);
    }

    private ModelAndView getModelAndView(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        }

        return wrapControllerInModelAndView((Controller) handler, request, response);
    }

    private ModelAndView wrapControllerInModelAndView(Controller handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ModelAndViewWrapper.wrap(handler.execute(request, response), JspView.class);
    }
}
