package core.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Set<Object> controllers = ControllerCollector.collect(basePackage);
        controllers.stream().map(this::mapToHandlerExecutions)
                .forEach(handlerExecutions::putAll);
    }

    private Map<HandlerKey, HandlerExecution> mapToHandlerExecutions(Object controller) {
        final Set<Method> actionMethods = findActionMethods(controller.getClass());
        Map<HandlerKey, HandlerExecution> handlerMap = Maps.newHashMap();
        actionMethods.forEach(method -> {
            final Map<HandlerKey, HandlerExecution> handlers = createHandlerExecutions(controller, method);
            handlerMap.putAll(handlers);
        });
        return handlerMap;
    }

    private Map<HandlerKey, HandlerExecution> createHandlerExecutions(Object controller, Method method) {
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        final Set<HandlerKey> handlerKeys = createHandlerKeys(method);
        final Map<HandlerKey, HandlerExecution> handlers = Maps.newHashMap();
        for(HandlerKey handlerKey : handlerKeys) {
            handlers.put(handlerKey, handlerExecution);
        }
        return handlers;
    }

    private Set<Method> findActionMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private Set<HandlerKey> createHandlerKeys(Method actionMethod) {
        final RequestMapping annotation = actionMethod.getAnnotation(RequestMapping.class);
        final String requestUri = annotation.value();
        final RequestMethod requestMethod = annotation.method();

        if(RequestMethod.ALL == requestMethod) {
            return HandlerKey.createWithAnyRequestMethod(requestUri);
        }
        final HandlerKey key = new HandlerKey(requestUri, requestMethod);
        return Sets.newHashSet(key);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}