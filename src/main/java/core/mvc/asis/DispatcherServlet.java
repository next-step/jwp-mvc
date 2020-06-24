package core.mvc.asis;

import core.mvc.HandlerMappings;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
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
    private static final String CONTROLLER_PACKAGE = "next.controller";

    private HandlerMappings handlerMappings;

    @Override
    public void init() {
        handlerMappings = new HandlerMappings();
        handlerMappings.add(new LegacyHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping(CONTROLLER_PACKAGE));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            HandlerExecution handler = handlerMappings.getHandler(req);
            ModelAndView modelAndView = handler.handle(req, resp);
            modelAndView.render(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : ", e);
            throw new ServletException(e.getMessage());
        }
    }

}
