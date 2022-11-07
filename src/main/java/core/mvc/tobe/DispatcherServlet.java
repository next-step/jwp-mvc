package core.mvc.tobe;

import core.mvc.view.ModelAndView;
import core.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(req);
            ModelAndView modelAndView = handlerExecution.handle(req, resp);

            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception: {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }
}
