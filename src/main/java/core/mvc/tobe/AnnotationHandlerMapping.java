package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Set<Class<?>> controllerTypes = findControllerTypes();

        Set<Method> methods = findRequestMappingMethods(controllerTypes);

        initHandlerExecutions(methods);
    }

    private Set<Method> findRequestMappingMethods(Set<Class<?>> controllers) {
        return controllers.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> findControllerTypes() {
        return Arrays.stream(basePackage)
                .map(Reflections::new)
                .map(reflection -> reflection.getTypesAnnotatedWith(Controller.class))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private void initHandlerExecutions(Set<Method> methods) {
        for (Method method : methods) {
            putHandlerExecution(method);
        }
    }

    private void putHandlerExecution(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());

        HandlerExecution handlerExecution = new HandlerExecution(createHandlerInstance(method), method);

        this.handlerExecutions.put(handlerKey, handlerExecution);
    }

    private Object createHandlerInstance(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        try {
            return declaringClass.newInstance();
        } catch (Exception e) {
            return new RuntimeException(e);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
