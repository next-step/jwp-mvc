package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.JspView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String HANDLER_MAPPING_PACKAGE = "next.controller";

    private LegacyHandlerMapping legacyHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;
    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        handlerMappings.add(legacyHandlerMapping);

        annotationHandlerMapping = new AnnotationHandlerMapping(HANDLER_MAPPING_PACKAGE);
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        Controller controller = legacyHandlerMapping.getHandler(req);
        try {
            final Object handler = findHandler(req);
            final ModelAndView modelAndView = handle(handler, req, resp);
            modelAndView.renderView(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(req)))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .orElseThrow(NoSuchMethodError::new);
    }

    private ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handler instanceof Controller) {
            final String veiwName = ((Controller) handler).execute(req, resp);
            return new ModelAndView(new JspView(veiwName));
        }
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(req, resp);
        }
        throw new NoSuchElementException("Resource doesn't exists.");
    }
}
