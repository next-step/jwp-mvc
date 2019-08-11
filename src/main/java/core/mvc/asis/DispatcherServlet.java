package core.mvc.asis;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
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
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestMapping rm;
    private AnnotationHandlerMapping ahm;

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        rm.initMapping();
        ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = rm.findController(requestUri);
        if (controller != null) {
            render(req, resp, controller);
        } else {
            HandlerExecution handlerExecution = ahm.getHandler(req);
            render(req, resp, handlerExecution);
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, HandlerExecution handlerExecution) throws ServletException {
        try {
            ModelAndView modelAndView = handlerExecution.handle(req, resp);
            View view = modelAndView.getView();
            ((View) view).render(modelAndView.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, Controller controller) throws ServletException {
        try {
            String viewName = controller.execute(req, resp);
            View view = new JspView(viewName);
            view.render(new HashMap<>(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
