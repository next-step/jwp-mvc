package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.springframework.beans.factory.BeanCreationException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationHandlerMapping {

    private static final Class<?>[] EMPTY_CLASS_ARRAY = {};

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        handlerExecutions.putAll(handlerKeyExecutionsMap());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private Map<HandlerKey, HandlerExecution> handlerKeyExecutionsMap() {
        Map<Class<?>, Object> controllerClassInstances = controllerClassInstances();
        return controllerClassInstances.keySet()
                .stream()
                .flatMap(controllerClass -> Stream.of(controllerClass.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toMap(this::toHandlerKey, method ->
                        new HandlerExecution(controllerClassInstances.get(method.getDeclaringClass()), method)));
    }

    private Map<Class<?>, Object> controllerClassInstances() {
        return new Reflections(basePackage).getTypesAnnotatedWith(Controller.class)
                .stream()
                .collect(Collectors.toMap(controllerClass -> controllerClass, this::newInstance));
    }

    private Object newInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor(EMPTY_CLASS_ARRAY).newInstance();
        } catch (Exception e) {
            throw new BeanCreationException(controllerClass.getName(), String.format("an error occurred during instance(%s) creation", controllerClass), e);
        }
    }

    private HandlerKey toHandlerKey(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }
}
