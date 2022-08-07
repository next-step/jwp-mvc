package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMethod;
import core.di.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    public static final List<RequestMethod> DEFAULT_REQUEST_METHODS = List.of(RequestMethod.values());

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {

    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private Map<HandlerKey, HandlerExecution> handlerKeyExecutionsMap() {
        return beanFactory.beansWithAnnotationType(Controller.class)
                .keySet()
                .stream()
                .flatMap(controllerClass -> Stream.of(controllerClass.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .flatMap(method -> keyExecutions(method).stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Map.Entry<HandlerKey, HandlerExecution>> keyExecutions(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
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
        return new HandlerExecution(beanFactory.getBean(method.getDeclaringClass()), method);
    }
}
