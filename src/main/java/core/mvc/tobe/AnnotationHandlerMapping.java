package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.RequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements RequestHandler {
    private static final int DEFAULT_REQUEST_METHOD_COUNT = 1;

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        Set<Method> methods = getMethods(controllers);
        methods.forEach(method -> generateHandlerExecutions(controllers, method));
    }

    private Set<Method> getMethods(Map<Class<?>, Object> controllers) {
        Set<Method> annotatedMethods = new HashSet<>();
        for (Class<?> clazz : controllers.keySet()) {
            Method[] methods = clazz.getDeclaredMethods();
            annotatedMethods.addAll(getAnnotatedMethods(methods));
        }

        return annotatedMethods;
    }

    private Set<Method> getAnnotatedMethods(Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void generateHandlerExecutions(Map<Class<?>, Object> controllers, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length < DEFAULT_REQUEST_METHOD_COUNT) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = getHandlerKey(requestMapping.value(), requestMethod);
            Object controller = controllers.get(method.getDeclaringClass());

            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    private HandlerKey getHandlerKey(String value, RequestMethod requestMethod) {
        return new HandlerKey(value, requestMethod);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        for (HandlerKey key : handlerExecutions.keySet()) {
            if (key.isUrlMatch(requestUri) && key.isMethodMatch(rm))
                return handlerExecutions.get(key);
        }

        return null;
    }
}
