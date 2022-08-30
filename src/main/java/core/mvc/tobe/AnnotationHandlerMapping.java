package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllerClassByInstance = controllerScanner.getControllerClassByInstance();

        Set<Method> requestMappingMethod = getRequestMappingMethods(controllerClassByInstance);

        for (Method method : requestMappingMethod) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            handlerExecutions.put(
                    getHandlerKey(requestMapping),
                    getHandlerExecution(controllerClassByInstance.get(method.getDeclaringClass()), method)
            );
        }
    }

    private HandlerKey getHandlerKey(RequestMapping requestMapping) {
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    private HandlerExecution getHandlerExecution(Object classInstance, Method method) {
        return new HandlerExecution(classInstance, method);
    }

    private Set<Method> getRequestMappingMethods(Map<Class<?>, Object> controllerClassByInstance) {
        Set<Method> requestMappingMethods = new HashSet<>();

        for (Class<?> controller : controllerClassByInstance.keySet()) {
            requestMappingMethods.addAll(selectRequestMappingMethods(controller));
        }

        return Collections.unmodifiableSet(requestMappingMethods);
    }

    private Set<Method> selectRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
