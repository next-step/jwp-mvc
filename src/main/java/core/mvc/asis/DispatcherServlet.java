package core.mvc.asis;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private RequestMapping rm;
    private AnnotationHandlerMapping annotationHandlerMapping;


    @Override
    public void init() {
        rm = new RequestMapping();
        // rm.initMapping();
        annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = rm.findController(requestUri);
        try {
            handle(controller, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handle(Controller controller, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (controller != null) {
            String viewName = controller.execute(req, resp);
            render(req, resp, new ModelAndView(new JspView(viewName)));
        } else {
            HandlerExecution handler = annotationHandlerMapping.getHandler(req);
            render(req, resp, handler.handle(req, resp));
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }
}
