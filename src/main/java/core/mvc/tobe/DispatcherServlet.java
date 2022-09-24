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
    public static final String SCAN_PACKAGE = "next.controller";
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private final HandlerMapping handlerMapping = new AnnotationHandlerMapping(SCAN_PACKAGE);

    @Override
    public void init() throws ServletException {
        handlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        HandlerExecution handler = handlerMapping.getHandler(req);
        if (Objects.nonNull(handler)) {
            try {
                ModelAndView modelAndView = handler.handle(req, resp);
                View view = modelAndView.getView();
                view.render(modelAndView.getModel(), req, resp);
            } catch (Exception e) {
                throw new RuntimeException("잘못 된 요청입니다 : " + e);
            }
        }
    }
}
