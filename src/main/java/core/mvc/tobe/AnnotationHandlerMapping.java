package core.mvc.tobe;

import static org.reflections.ReflectionUtils.Methods;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class AnnotationHandlerMapping {

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
            final Set<Method> methods = ReflectionUtils.get(Methods.of(controller.getClass(), withAnnotation(RequestMapping.class)));
            mappingHandler(controller, methods);
        }
    }

    private void mappingHandler(final Object handler, final Set<Method> methods) {
        for (final Method method : methods) {
            final List<HandlerKey> handlerKeys = createHandlerKeys(method);
            final HandlerExecutable handlerExecution = new HandlerExecution(handler, method);
            mappingHandler(handlerKeys, handlerExecution);
        }
    }

    private static List<HandlerKey> createHandlerKeys(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        final RequestMethod[] requestMethods = requestMapping.method();
        final String url = requestMapping.value();

        if (requestMethods.length == 0) {
            return createHandlerKeys(url, RequestMethod.values());
        }

        return createHandlerKeys(url, requestMethods);
    }

    private static List<HandlerKey> createHandlerKeys(final String url, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
            .map(requestMethod -> new HandlerKey(url, requestMethod))
            .collect(Collectors.toList());
    }

    private void mappingHandler(final List<HandlerKey> handlerKeys, final HandlerExecutable handlerExecution) {
        for (final HandlerKey handlerKey : handlerKeys) {
            HANDLER_EXECUTIONS.put(handlerKey, handlerExecution);
        }
    }

    public HandlerExecutable getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return HANDLER_EXECUTIONS.get(new HandlerKey(requestUri, rm));
    }
}
