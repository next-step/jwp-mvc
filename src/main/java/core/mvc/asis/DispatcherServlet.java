package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private LegacyHandlerMapping rm;
    private AnnotationHandlerMapping ahm;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() {
        rm.initMapping();
        handlerMappings.add(rm);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        final var controller = findController(req);

        final ModelAndView modelAndView = execute(controller, req, resp);

        try {
            modelAndView.render(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView execute(final Object controller, final HttpServletRequest req, final HttpServletResponse resp) {
        if (controller instanceof HandlerExecution) {
            try {
                return ((HandlerExecution) controller).handle(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException();
    }

    private Object findController(final HttpServletRequest req) {
        return handlerMappings.stream()
            .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(req)))
            .findFirst()
            .map(handlerMapping -> handlerMapping.getHandler(req))
            .orElseThrow(NoSuchMethodError::new);
    }

}
