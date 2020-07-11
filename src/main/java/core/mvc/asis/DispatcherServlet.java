package core.mvc.asis;

import core.mvc.Controller;
import core.mvc.HandlerMappings;
import core.mvc.tobe.AnnotationHandlerMapping;
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

    private HandlerMappings handlerMappings;

    @Override
    public void init() {
        LegacyHandlerMapping lrm = new LegacyHandlerMapping();
        lrm.initMapping();
        handlerMappings.add(lrm);

        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
        handlerMappings.add(ahm);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        Controller handler = getHandler(req);
        executeController(req, resp, handler);
    }

    private void executeController(final HttpServletRequest req, final HttpServletResponse resp, final Controller handler) throws ServletException {
        try {
            String viewName = handler.execute(req, resp);
            move(viewName, req, resp);
        } catch (Exception e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Controller getHandler(final HttpServletRequest req) {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        return handlerMappings.get(req);
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
