package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static org.reflections.ReflectionUtils.*;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
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
            HandlerKey key = new HandlerKey(annotation.value(), annotation.method());
            HandlerExecution execution = new HandlerExecution(handler, method);
            handlerExecutions.put(key, execution);
        });
    }

    private Object createHandlerInstance(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
