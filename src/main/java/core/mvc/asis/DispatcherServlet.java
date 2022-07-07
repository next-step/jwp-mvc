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
import java.util.Objects;

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

        annotationHandlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = rm.findController(requestUri);
        if (Objects.nonNull(controller)) {
            try {
                String viewName = controller.execute(req, resp);
                move(viewName, req, resp);
                return;
            } catch (Throwable e) {
                logger.error("Exception : {}", e);
                throw new ServletException(e.getMessage());
            }
        }

        HandlerExecution handler = annotationHandlerMapping.getHandler(req);
        try {
            ModelAndView modelAndView = handler.handle(req, resp);
            Map<String, Object> model = modelAndView.getModel();
            View view = modelAndView.getView();
            view.render(model, req, resp);
        } catch (Throwable e) {
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
