package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.ModelView;
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

    private RequestMapping rm;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        rm.initMapping();
        annotationHandlerMapping = new AnnotationHandlerMapping("core.mvc.asis", "next.controller");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        ModelAndView modelAndView;
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);
        try {
            if (annotationHandlerMapping.containsExecution(req)) {
                HandlerExecution execution = annotationHandlerMapping.getHandler(req);
                modelAndView = execution.handle(req, resp);
            } else {
                Controller controller = rm.findController(requestUri);
                String viewName = controller.execute(req, resp);
                modelAndView = new ModelAndView(viewName);
            }
            this.viewRender(modelAndView, req, resp);
        } catch (Exception e) {
            logger.error("dispatch service error : {}", e);
        }
    }

    private void viewRender(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }

}
