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
    private static final String CONTROLLER_PACKAGE = "next.controller";

    private LegacyHandlerMapping legacyHandlerMapping;
    private AnnotationHandlerMapping handlerMapping;

    @Override
    public void init() throws ServletException {
        legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        handlerMapping = new AnnotationHandlerMapping(CONTROLLER_PACKAGE);
        handlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = legacyHandlerMapping.getHandler(req);
        try {
            if (controller != null) {
                render(req, resp, controller.execute(req, resp));
                return;
            }
            HandlerExecution execution = handlerMapping.getHandler(req);
            render(req, resp, execution.handle(req, resp));
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, String view) throws Exception {
        if (view.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(view.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(view);
        rd.forward(req, resp);
    }
}
