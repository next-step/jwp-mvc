package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

public class AnnotationHandlerMapping {
    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);

        for (Class<?> controllerClass : controllers) {
            final Object controllerInstance = newInstance(controllerClass);
            final Set<Method> handlers = getAllMethods(controllerClass, withAnnotation(RequestMapping.class));

            addHandlerExecutions(controllerInstance, handlers);
        }
    }

    private Object newInstance(Class<?> controllerClass) {
        Object instance = null;
        try {
            Constructor constructor = controllerClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private void addHandlerExecutions(Object controllerInstance, Set<Method> handlers) {
        for (Method handler : handlers) {
            addHandlerExecution(controllerInstance, handler);
        }
    }

    private void addHandlerExecution(Object controllerInstance, Method handler) {
        final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, handler);

        final RequestMapping requestMapping = handler.getAnnotation(RequestMapping.class);
        final String path = requestMapping.value();

        final List<RequestMethod> httpMethods = extractHandlerMethods(requestMapping);
        for (RequestMethod httpMethod : httpMethods) {
            final HandlerKey handlerKey = new HandlerKey(path, httpMethod);
            if (httpMethods.size() == 1 || handlerExecutions.get(handlerKey) == null) {
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private List<RequestMethod> extractHandlerMethods(RequestMapping requestMapping) {
        List<RequestMethod> requestMethods = Arrays.asList(requestMapping.method());
        if (requestMethods.isEmpty()) {
            requestMethods = Arrays.asList(RequestMethod.values());
        }
        return requestMethods;
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
