package core.mvc.asis;

import core.mvc.HandlerMappings;
import core.mvc.ModelAndView;
import core.mvc.resolver.HandlerMethodArgumentResolverMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;


@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String HANDLER_MAPPING_PACKAGE = "next.controller";

    private HandlerMethodArgumentResolverMapping argumentResolverMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;
    private final HandlerMappings handlerMappings = new HandlerMappings();

    @Override
    public void init() throws ServletException {
        annotationHandlerMapping = new AnnotationHandlerMapping(HANDLER_MAPPING_PACKAGE);
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);

        argumentResolverMapping = new HandlerMethodArgumentResolverMapping();
        argumentResolverMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            final Object handler = handlerMappings.findHandler(req);
            final ModelAndView modelAndView = handle(handler, req, resp);
            modelAndView.renderView(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Object[] values = argumentResolverMapping.resolve(((HandlerExecution) handler).getMethod(), req);
        return ((HandlerExecution) handler).handle(values);
    }
}
