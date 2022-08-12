package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import org.reflections.ReflectionUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    public static final List<RequestMethod> DEFAULT_REQUEST_METHODS = List.of(RequestMethod.values());
    public static final Class<RequestMapping> FOUND_REQUEST_MAPPING_METHOD_ANNOTATION = RequestMapping.class;

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(ControllerScanner controllerScanner) {
        this.controllerScanner = controllerScanner;
    }

    public void initialize() {
        handlerExecutions.putAll(handlerKeyExecutionsMap());
    }

    @Override
    public boolean isSupported(HttpServletRequest request) {
        HandlerKey handlerKey = handlerKey(request);
        return handlerExecutions.keySet()
                .stream()
                .anyMatch(key -> key.matches(handlerKey));
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = handlerKey(request);
        return handlerExecutions.entrySet()
                .stream()
                .filter(entry -> entry.getKey().matches(handlerKey))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("unsupported request(%s). isSupported method should be called first", request)));
    }

    private HandlerKey handlerKey(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return new HandlerKey(requestUri, rm);
    }

    private Map<HandlerKey, HandlerExecution> handlerKeyExecutionsMap() {
        return controllerScanner.controllers()
                .keySet()
                .stream()
                .flatMap(controllerClass -> methodsWithRequestMapping(controllerClass).stream())
                .flatMap(method -> keyExecutions(method).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Collection<Method> methodsWithRequestMapping(Class<?> controllerClass) {
        return ReflectionUtils.getMethods(controllerClass,
                ReflectionUtils.withAnnotation(FOUND_REQUEST_MAPPING_METHOD_ANNOTATION));
    }

    private List<Map.Entry<HandlerKey, HandlerExecution>> keyExecutions(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(FOUND_REQUEST_MAPPING_METHOD_ANNOTATION);
        return requestMethods(requestMapping)
                .stream()
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .map(key -> new AbstractMap.SimpleEntry<>(key, toHandlerExecution(method)))
                .collect(Collectors.toList());
    }

    private List<RequestMethod> requestMethods(RequestMapping requestMapping) {
        List<RequestMethod> methods = List.of(requestMapping.method());
        if (methods.isEmpty()) {
            return DEFAULT_REQUEST_METHODS;
        }
        return methods;
    }

    private HandlerExecution toHandlerExecution(Method method) {
        return new HandlerExecution(controllerInstance(method.getDeclaringClass()), method);
    }

    private Object controllerInstance(Class<?> clazz) {
        return controllerScanner.instance(clazz)
                .orElseThrow(() -> new NoSuchBeanDefinitionException(clazz));
    }
}
