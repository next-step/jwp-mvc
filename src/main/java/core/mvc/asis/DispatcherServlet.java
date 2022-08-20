package core.mvc.asis;

import core.exception.NotExistHandlerException;
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
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE_PATH = "next.controller";

    private List<RequestMapping> requestMappings;
    @Override
    public void init() {
        requestMappings = Arrays.asList(new LegacyRequestMapping(), new AnnotationHandlerMapping(BASE_PACKAGE_PATH));
        requestMappings.forEach(RequestMapping::initialize);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            handleRequestMapping(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handleRequestMapping(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        for (RequestMapping requestMapping : requestMappings) {
            Object handler = requestMapping.findHandler(req);
            if (handler instanceof Controller) {
                controllerHandle(requestMapping, req, resp);
                return;
            } else if (handler instanceof HandlerExecution) {
                annotationHandle(requestMapping, req, resp);
                return;
            } else {
                throw new NotExistHandlerException("Handler is not exist");
            }
        }
    }

    private void annotationHandle(RequestMapping requestMapping, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) requestMapping.findHandler(req);
        ModelAndView modelAndView = handlerExecution.handle(req, resp);
        modelAndView.render(req, resp);
    }

    private void controllerHandle(RequestMapping requestMapping, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Controller controller = (Controller) requestMapping.findHandler(req);
        ModelAndView modelAndView = new ModelAndView(controller.execute(req, resp));
        modelAndView.render(req, resp);
    }
}
