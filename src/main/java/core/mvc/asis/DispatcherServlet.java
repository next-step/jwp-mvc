package core.mvc.asis;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.tobe.AnnotationHandlerAdapter;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerAdapter;
import core.mvc.tobe.HandlerMapping;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> adapters = List.of(new LegacyHandlerAdapterAdapter(),
        new AnnotationHandlerAdapter());

    @Override
    public void init() throws ServletException {
        var legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();

        var annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();

        this.handlerMappings = List.of(legacyHandlerMapping, annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            var handler = handlerMappings.stream()
                .map(it -> it.getHandler(req))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 URL 입니디. url" + requestUri));

            var handlerAdapter = adapters.stream()
                .filter(it -> it.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 핸들러입니다. url" + requestUri));

            var modelAndView = handlerAdapter.handle(req, resp, handler);
            modelAndView.render(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
