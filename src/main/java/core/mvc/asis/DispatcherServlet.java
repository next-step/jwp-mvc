package core.mvc.asis;

import core.mvc.handler.HandlerExecutor;
import core.mvc.mapping.MappingRegistry;
import core.mvc.mapping.MappingRegistryFactory;
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

    private HandlerExecutor handlerExecutor;

    @Override
    public void init() throws ServletException {
        MappingRegistry registry = MappingRegistryFactory.create("next.controller");
        handlerExecutor = new HandlerExecutor(registry);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            handlerExecutor.execute(req, resp);
        } catch (Throwable e) {
            logger.error("Exception!!!", e);
            throw new ServletException(e.getMessage());
        }
    }

}
