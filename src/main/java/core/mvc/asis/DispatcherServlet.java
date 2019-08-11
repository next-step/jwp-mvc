package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import next.WebServerLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestMapping rm;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        rm.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping(WebServerLauncher.class);

        try {
            annotationHandlerMapping.initialize();
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException : {}", e.getMessage());
            throw new ServletException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            final HandlerExecution handler = annotationHandlerMapping.getHandler(req);
            if (handler != null) {
                execute(req, resp, handler);
                return;
            }

            final Controller controller = rm.findController(requestUri);
            if (controller != null) {
                executeLegacy(req, resp, controller);
                return;
            }

            throw new ServletException("페이지를 찾을 수 없습니다.");
        } catch (Throwable e) {
            logger.error("Exception : {}", e.getMessage());
            throw new ServletException(e);
        }
    }

    private void execute(HttpServletRequest req, HttpServletResponse resp, HandlerExecution handler) throws Exception {
        final ModelAndView modelAndView = handler.handle(req, resp);

        final Map<String, Object> model = modelAndView.getModel();
        final View view = modelAndView.getView();

        view.render(model, req, resp);
    }

    private void executeLegacy(HttpServletRequest req, HttpServletResponse resp, Controller controller) throws Exception {
        final String viewName = controller.execute(req, resp);
        move(viewName, req, resp);
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
