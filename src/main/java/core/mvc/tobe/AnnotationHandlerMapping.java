package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        final Set<Object> controllers = ControllerScanner.scan(basePackage);
        for (Object controller : controllers) {
            mapToHandlerExecutions(controller);
        }
    }

    private void mapToHandlerExecutions(Object controller) {
        final Class<?> controllerClass = controller.getClass();
        final Set<Method> actionMethods = getActionMethods(controllerClass.getMethods());
        for (Method method : actionMethods) {
            appendHandlerExecutions(controller, method);
        }
    }

    private void appendHandlerExecutions(Object controller, Method method) {
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final boolean isFallbackMapping = isFallbackMethod(requestMapping);
        final HandlerKey[] handlerKeys = createHandlerKeys(requestMapping, isFallbackMapping);

        if (!isFallbackMethod(requestMapping)) {
            for (HandlerKey handlerKey : handlerKeys) {
                handlerExecutions.put(handlerKey, handlerExecution);
            }
            return;
        }

        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
        }
    }

    private boolean isFallbackMethod(RequestMapping requestMapping) {
        return requestMapping.method().length == 0;
    }

    private Set<Method> getActionMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private HandlerKey[] createHandlerKeys(RequestMapping requestMapping, boolean isFallbackMapping) {
        final String requestUri = requestMapping.value();
        final RequestMethod[] requestMethods = isFallbackMapping ? RequestMethod.values() : requestMapping.method();
        return Arrays.stream(requestMethods)
                .map(m -> new HandlerKey(requestUri, m))
                .toArray(HandlerKey[]::new);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}