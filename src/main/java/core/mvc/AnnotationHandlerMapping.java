package core.mvc;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class AnnotationHandlerMapping extends HandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        new Reflections(basePackage)
                .getTypesAnnotatedWith(Controller.class)
                .forEach(this::putHandlerExecutionsByController);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod().toUpperCase())
        );
        return handlerExecutions.entrySet().stream()
                .filter(it -> it.getKey().isMatch(key))
                .map(it -> it.getValue())
                .findAny()
                .orElse(null);
    }

    private void putHandlerExecutionsByController(Class<?> controllerClass) {
        Object target;
        try {
            target = controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            return;
        }

        ReflectionUtils.getAllMethods(
                controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        ).forEach(method -> putHandlerExecutionsByMethod(target, method));
    }

    private void putHandlerExecutionsByMethod(Object target, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method().length > 0
                ? requestMapping.method()
                : RequestMethod.values();
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(
                    new HandlerKey(url, requestMethod),
                    new HandlerExecution(target, method)
            );
        }
    }
}
