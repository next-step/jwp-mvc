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
    private final Map<String, HandlerExecution> fallbackHandlerExecutions = Maps.newHashMap();

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

        if (isFallbackMethod(method)) {
            String requestUrl = requestMapping.value();
            fallbackHandlerExecutions.put(requestUrl, handlerExecution);
            return;
        }

        final HandlerKey[] handlerKeys = createHandlerKeys(requestMapping);
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private boolean isFallbackMethod(Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        return annotation.method().length == 0;
    }

    private Set<Method> getActionMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private HandlerKey[] createHandlerKeys(RequestMapping annotation) {
        final String requestUri = annotation.value();
        final RequestMethod[] requestMethods = annotation.method();
        return Arrays.stream(requestMethods)
                .map(m -> new HandlerKey(requestUri, m))
                .toArray(HandlerKey[]::new);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        HandlerExecution handlerExecution = handlerExecutions.get(new HandlerKey(requestUri, rm));
        if (handlerExecution != null) {
            return handlerExecution;
        }
        return fallbackHandlerExecutions.get(requestUri);
    }
}