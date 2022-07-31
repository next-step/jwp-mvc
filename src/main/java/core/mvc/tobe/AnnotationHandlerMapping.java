package core.mvc.tobe;

import static org.reflections.ReflectionUtils.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.reflections.Reflections;

import com.google.common.collect.Maps;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

public class AnnotationHandlerMapping {
    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
            .forEach(this::mappingHandlerExecutions);
    }

    private void mappingHandlerExecutions(Class<?> controller) {
        Set<Method> methods = get(Methods.of(controller, withAnnotation(RequestMapping.class)));
        Object handler = createHandlerInstance(controller);
        methods.forEach(method -> {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            HandlerKey handlerKey = new HandlerKey(annotation.value(), annotation.method());
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        });
    }

    private Object createHandlerInstance(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
