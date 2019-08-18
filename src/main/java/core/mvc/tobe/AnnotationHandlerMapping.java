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

public class AnnotationHandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Set<Object> controllers = ControllerScanner.scan(basePackage);
        controllers.stream().map(this::mapToHandlerExecutions)
                .forEach(handlerExecutions::putAll);
    }

    private Map<HandlerKey, HandlerExecution> mapToHandlerExecutions(Object controller) {
        final Class<?> controllerClass = controller.getClass();
        final Set<Method> actionMethods = filterActionMethods(controllerClass.getMethods());
        Map<HandlerKey, HandlerExecution> handlerMap = Maps.newHashMap();
        actionMethods.forEach(method -> {
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            final HandlerKey handlerKey = createHandlerKey(method.getAnnotation(RequestMapping.class));
            handlerMap.put(handlerKey, handlerExecution);
        });
        return handlerMap;
    }

    private Set<Method> filterActionMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private HandlerKey createHandlerKey(RequestMapping annotation) {
        final String requestUri = annotation.value();
        final RequestMethod requestMethod = annotation.method();
        return new HandlerKey(requestUri, requestMethod);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        final HandlerExecution handlerExecution = handlerExecutions.get(new HandlerKey(requestUri, rm));
        if (handlerExecution != null) {
            return handlerExecution;
        }
        return handlerExecutions.get(new HandlerKey(requestUri, RequestMethod.DEFAULT));
    }
}