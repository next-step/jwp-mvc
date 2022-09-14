package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final HandlerMapping handlerMapping = new AnnotationHandlerMapping("next.controller");

    @Override
    public void init() {
        handlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        HandlerExecution handlerExecution = handlerMapping.getHandler(req);

        if (Objects.nonNull(handlerExecution)) {
            renderingByView(handlerExecution, req, resp);
        }
    }

    private void renderingByView(HandlerExecution handlerExecution, HttpServletRequest req, HttpServletResponse resp) {
        try {
            ModelAndView modelAndView = handlerExecution.handle(req, resp);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), req, resp);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
