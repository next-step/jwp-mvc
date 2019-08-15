package core.mvc.asis;

import core.mvc.tobe.HandlerAdapter;
import core.mvc.tobe.HandlerMappings;
import core.mvc.tobe.ServletHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private HandlerMappings handlerMappings;
    private HandlerAdapter handlerAdapter;

    @Override
    public void init() throws ServletException {
        handlerMappings = HandlerMappings.of();
        handlerAdapter = ServletHandlerAdapter.of(handlerMappings);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            execute(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handlerAdapter.support(req)) {
            Object handler = handlerMappings.getHandler(req);
            handlerAdapter.handle(req, resp, handler);
        }
    }
}
