package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMethod;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final HandlerExecutable NOT_FOUND_EXECUTION = new NotFoundExecution();

    private final Object[] basePackage;

    private static final Map<HandlerKey, HandlerExecutable> HANDLER_EXECUTIONS = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        initialize();
    }

    private void initialize() {
        final Set<Object> controllers = ControllerScanner.getControllers(new Reflections(basePackage));

        for (final Object controller : controllers) {
            final Map<HandlerKey, HandlerExecutable> handlerExecutable = RequestMappingScanner.getHandlerExecutable(controller);
            HANDLER_EXECUTIONS.putAll(handlerExecutable);
        }
        logger.info("Initialized Annotation Handler Mapping!");
        HANDLER_EXECUTIONS.keySet().forEach(key -> logger.info("Path : {}, Controller : {}", key, HANDLER_EXECUTIONS.get(key)));
    }

    @Override
    public HandlerExecutable getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        final HandlerKey key = new HandlerKey(requestUri, rm);

        return HANDLER_EXECUTIONS.keySet().stream()
            .filter(it -> it.equals(key))
            .findAny()
            .map(HANDLER_EXECUTIONS::get)
            .orElse(NOT_FOUND_EXECUTION);
    }
}
