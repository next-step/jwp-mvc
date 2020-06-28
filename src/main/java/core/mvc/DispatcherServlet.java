package core.mvc;

import core.mvc.asis.Controller;
import core.mvc.asis.LegacyHandlerMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private LegacyHandlerMapping legacyHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    private HandlerMappings handlerMappings = new HandlerMappings();

    @Override
    public void init() {
        legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        handlerMappings.add(legacyHandlerMapping);

        annotationHandlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        try {
            HandlerExecution handlerExecution = getHandler(req);
            ModelAndView modelAndView = handlerExecution.handle(req, resp);
            modelAndView.render(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HandlerExecution getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        Controller controller = legacyHandlerMapping.findController(requestUri);
        return handlerMappings.getHandlerMapping(controller)
                .getHandler(request);
    }
}
