package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final View EMPTY_VIEW = (model, request, response) -> {};

    private RequestMapping rm;
    private AnnotationHandlerMapping am;

    @Override
    public void init(){
        rm = new RequestMapping();
        rm.initMapping();
        am = new AnnotationHandlerMapping("core.next");
        am.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        HandlerExecution execution = am.getHandler(req);
        if (execution != null) {
            handle(execution, req, resp);
            return;
        }

        Controller controller = rm.findController(requestUri);
        handle(controller, req, resp);
    }

    private void handle(Controller controller, HttpServletRequest req, HttpServletResponse resp)
        throws ServletException {
        try {
            String viewName = controller.execute(req, resp);
            move(viewName, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handle(HandlerExecution execution , HttpServletRequest req, HttpServletResponse resp)
        throws ServletException {
        try {
            ModelAndView modelAndView = execution.handle(req, resp);
            Optional.ofNullable(modelAndView.getView()).orElse(EMPTY_VIEW)
                .render(modelAndView.getModel(), req, resp);
        } catch (Exception e) {
            logger.error("Exception : {}", e);
            throw new ServletException();
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
