package core.mvc.asis;

import core.mvc.ModelAndView;
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

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private LegacyHandlerMapping legacyHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        final HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(request);
        if (handlerExecution != null) {
            executeAnnotationHandler(request, response, handlerExecution);
            return;
        }

        final Controller controller = legacyHandlerMapping.getHandler(request);
        executeController(request, response, controller);
    }

    private void executeAnnotationHandler(HttpServletRequest request, HttpServletResponse response, HandlerExecution handlerExecution) throws ServletException {
        try {
            final ModelAndView modelAndView = handlerExecution.handle(request, response);
            modelAndView.render(request, response);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void executeController(HttpServletRequest request, HttpServletResponse response, Controller controller) throws ServletException {
        try {
            String viewName = controller.execute(request, response);
            move(viewName, request, response);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(String viewName, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
