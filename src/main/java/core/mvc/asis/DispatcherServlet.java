package core.mvc.asis;

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
import java.util.Map;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String BASE_PACKAGE = "core.mvc.tobe";

    private RequestMapping rm;
    private AnnotationHandlerMapping ahm;

    @Override
    public void init() {
        rm = new RequestMapping();
        rm.initMapping();
        ahm = new AnnotationHandlerMapping(BASE_PACKAGE);
        ahm.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        boolean processed = handleToBe(req, resp);

        if (!processed) {
            handleAsIs(requestUri, req, resp);
        }
    }

    private boolean handleToBe(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final HandlerExecution handler = ahm.getHandler(req);
        if (handler == null) {
            return false;
        }
        try {
            final ModelAndView mav = handler.handle(req, resp);
            render(mav, req, resp);
        } catch (Throwable e) {
            throw new ServletException(e.getMessage());
        }
        return true;
    }

    private void handleAsIs(String requestUri, HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final Controller controller = rm.findController(requestUri);
        try {
            final String viewName = controller.execute(req, resp);
            move(viewName, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final Map<String, Object> model = mav.getModel();
        final View view = mav.getView();

        try {
            view.render(model, req, resp);
        } catch (Exception e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
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
