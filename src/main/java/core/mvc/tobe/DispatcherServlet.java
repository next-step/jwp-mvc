package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.View;
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
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private final HandlerMapping handlerMapping = new AnnotationHandlerMapping();

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
