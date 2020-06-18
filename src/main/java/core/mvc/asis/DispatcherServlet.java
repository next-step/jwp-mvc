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
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CONTROLLER_PATH = "next.controller";

    // TODO 추후 삭제
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping mapping;

    private RequestMapping rm;

    @Override
    public void init() throws ServletException {
        mapping = new AnnotationHandlerMapping(CONTROLLER_PATH);
        mapping.initialize();

        // TODO 추후 삭제
        rm = new RequestMapping();
        rm.initMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        // TODO 추후 삭제
        Controller controller = rm.findController(requestUri);
        if(Objects.nonNull(controller)) {
            try {
                String viewName = controller.execute(req, resp);
                move(viewName, req, resp);
            } catch (Throwable e) {
                logger.error("Exception : {}", e);
                throw new ServletException(e.getMessage());
            }
            return;
        }

        try {
            HandlerExecution execution = mapping.getHandler(req);
            ModelAndView modelAndView = execution.handle(req, resp);
            modelAndView.render(req, resp);
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
