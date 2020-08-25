package core.mvc.asis;

import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String DEFAULT_PACKAGE = "core.mvc.tobe";

    private List<RequestMapping> requestMappingList;

    @Override
    public void init() {
        final LegacyRequestMapping rm = new LegacyRequestMapping();
        rm.initialize();
        requestMappingList.add(rm);

        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping(DEFAULT_PACKAGE);
        ahm.initialize();
        requestMappingList.add(ahm);
    }

    @Override
    protected void service(final HttpServletRequest req,
                           final HttpServletResponse resp) throws ServletException {
        Object handler = getHandler(req);
        if (handler instanceof Controller) {
            executeController((Controller) handler, req, resp);
        } else if (handler instanceof HandlerExecution) {
            handleExecution((HandlerExecution) handler, req, resp);
        }
    }

    private Object getHandler(final HttpServletRequest req) {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);
        return requestMappingList.stream()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }


    private void executeController(final Controller controller,
                                   final HttpServletRequest req,
                                   final HttpServletResponse resp) throws ServletException {
        try {
            String viewName = controller.execute(req, resp);
            move(viewName, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handleExecution(final HandlerExecution execution,
                                 final HttpServletRequest req,
                                 final HttpServletResponse resp) throws ServletException {
        try {
            execution.handle(req, resp);
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
