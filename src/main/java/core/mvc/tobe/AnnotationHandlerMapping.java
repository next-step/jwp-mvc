package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMethod;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.reflections.Reflections;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;

    private static final Map<HandlerKey, HandlerExecutable> HANDLER_EXECUTIONS = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);
        controllerScanner.instantiateControllers();
        final Set<Object> controllers = controllerScanner.getControllers();

        for (final Object controller : controllers) {
            final Map<HandlerKey, HandlerExecutable> handlerExecutable = RequestMappingScanner.getHandlerExecutable(controller);
            HANDLER_EXECUTIONS.putAll(handlerExecutable);
        }
    }

    @Override
    public HandlerExecutable getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return HANDLER_EXECUTIONS.get(new HandlerKey(requestUri, rm));
    }
}
