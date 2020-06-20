package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;
    private HandlerExecutions handlerExecutions = new HandlerExecutions();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);

        for (Class<?> controllerClass : controllers) {
            final Object controllerInstance = newInstance(controllerClass);
            final Set<Method> handlers = getAllMethods(controllerClass, withAnnotation(RequestMapping.class));
            for (Method handler: handlers) {
                handlerExecutions.add(controllerInstance, handler);
            }
        }
    }

    private Object newInstance(Class<?> controllerClass) {
        Object instance = null;
        try {
            Constructor<?> constructor = controllerClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());

        final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
