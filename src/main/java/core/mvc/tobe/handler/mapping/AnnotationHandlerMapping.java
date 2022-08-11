package core.mvc.tobe.handler.mapping;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Class<Controller> ANNOTATION_CLASS_FOR_CONTROLLER = Controller.class;
    private static final Class<RequestMapping> ANNOTATION_CLASS_FOR_METHOD = RequestMapping.class;
    private final Reflections reflections;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public void initialize() {
        Set<Class<?>> invokerClasses = getInvokerClasses();
        for (Class<?> invokerClass : invokerClasses) {
            Object invoker = getInvokerObjectWithDefaultConstructor(invokerClass);
            Set<Method> allMethods = getHandlerMethods(invokerClass);
            addHandlerExecution(invoker, allMethods);
        }
    }

    private Set<Class<?>> getInvokerClasses() {
        return reflections.getTypesAnnotatedWith(ANNOTATION_CLASS_FOR_CONTROLLER);
    }

    private Object getInvokerObjectWithDefaultConstructor(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Controller 인스턴스 생성중 예외 발생", e);
        }
    }

    private Set<Method> getHandlerMethods(Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(ANNOTATION_CLASS_FOR_METHOD));
    }

    private void addHandlerExecution(Object invoker, Set<Method> methods) {
        for (Method method : methods) {
            addHandlerExecutionEachMethod(invoker, method);
        }
    }

    private void addHandlerExecutionEachMethod(Object invoker, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(ANNOTATION_CLASS_FOR_METHOD);
        String url = requestMapping.value();
        List<RequestMethod> requestMethods = getRequestMethods(requestMapping);
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(invoker, method));
        }
    }

    private List<RequestMethod> getRequestMethods(RequestMapping requestMapping) {
        List<RequestMethod> methods = List.of(requestMapping.method());
        if (methods.isEmpty()) {
            return List.of(RequestMethod.values());
        }

        return methods;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}
