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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private LegacyHandlerMapping legacyHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        final Object handler = findHandler(request);
        final ModelAndView mav = executeHandler(request, response, handler);
        mav.render(request, response);
    }

    private Object findHandler(HttpServletRequest request) throws ServletException {
        List<Object> handlers = new ArrayList<>();
        handlers.add(annotationHandlerMapping.getHandler(request));
        handlers.add(legacyHandlerMapping.getHandler(request));
        return handlers.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow((() -> new ServletException("요청한 URI를 처리할 수 있는 핸들러가 없습니다.")));
    }

    private ModelAndView executeHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        if (handler instanceof Controller) {
            return ((Controller) handler).execute(request, response);
        }

        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        }

        throw new ServletException("처리할 수 없는 핸들러 유형입니다.");
    }
}
