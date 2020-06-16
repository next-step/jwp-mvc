package core.mvc;

import core.mvc.asis.RequestMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

/**
 * Incrementally migrates a legacy module, RequestMapping for this case, by gradually replacing new mvc module.
 * This object intercepts the request and determines which handler should process the request.
 * <p>
 * Unlike its name, this is actually facade pattern. not proxy pattern.
 *
 * @author hyeyoom
 */
public class ProxyHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(ProxyHandlerMapping.class);

    private final RequestMapping rm;
    private final AnnotationHandlerMapping annotationHandlerMapping;

    public ProxyHandlerMapping() {
        rm = new RequestMapping();
        rm.initMapping();
        annotationHandlerMapping = new AnnotationHandlerMapping();
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        final Object handler = Optional
                .ofNullable(rm.getHandler(request))
                .orElse(annotationHandlerMapping.getHandler(request));
        Objects.requireNonNull(handler, "Handler not found.");
        return handler;
    }
}
