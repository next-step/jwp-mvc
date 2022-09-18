package core.mvc.asis;

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
import java.util.Optional;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final RequestMapping requestMapping = new RequestMapping();
    private final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");

    @Override
    public void init() {
        requestMapping.initMapping();
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = Optional.ofNullable(requestMapping.getHandler(req))
                .orElseGet(() -> annotationHandlerMapping.getHandler(req));

        try {
            if (handler instanceof Controller) {
                String viewName = ((Controller) handler).execute(req, resp);
                if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
                    resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
                }
                req.getRequestDispatcher(viewName).forward(req, resp);
            } else if (handler instanceof HandlerExecution) {
                ModelAndView modelAndView = ((HandlerExecution) handler).handle(req, resp);
                modelAndView.getView().render(modelAndView.getModel(), req, resp);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
