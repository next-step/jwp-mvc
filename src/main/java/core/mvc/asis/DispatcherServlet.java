package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.RequestMapping;
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
    private static final String BASE_PACKAGE_PATH = "next.controller";

    private RequestMapping requestMapping;

    private RequestMapping annotationHandlerMapping;

    @Override
    public void init() {
        requestMapping = new ClassRequestMapping();
        requestMapping.initialize();

        annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE_PATH);
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            if (annotationHandle(req, resp)) {
                return;
            }
            controllerHandle(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private boolean annotationHandle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) annotationHandlerMapping.findHandler(req);
        if (handlerExecution != null) {
            ModelAndView modelAndView = handlerExecution.handle(req, resp);
            modelAndView.render(req, resp);
            return true;
        }
        return false;
    }

    private void controllerHandle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Controller controller = (Controller) requestMapping.findHandler(req);
        ModelAndView modelAndView = new ModelAndView(controller.execute(req, resp));
        modelAndView.render(req, resp);
    }
}
