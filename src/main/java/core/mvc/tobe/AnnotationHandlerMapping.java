package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.exception.NotFoundException;
import core.mvc.RequestHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping implements RequestHandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecutions> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        validate(basePackage);

        this.basePackage = basePackage;
    }

    private void validate(Object[] basePackage) {
        if (Objects.isNull(basePackage) || basePackage.length == 0) {
            throw new IllegalArgumentException("BasePackage can't be empty");
        }
    }

    public void initialize() {
        Set<Class<?>> controllers = AnnotatedTargetScanner.loadClasses(Controller.class, basePackage);

        controllers.forEach(this::convertClassToHandlerExecution);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        return handlerExecutions.keySet()
                .stream()
                .filter(handlerKey -> handlerKey.isSupport(request))
                .map(handlerKey -> handlerExecutions.get(handlerKey).getHandler(request))
                .findFirst()
                .orElse(null);
    }

    private void convertClassToHandlerExecution(final Class<?> clazz) {
        Object instance = newInstance(clazz);

        AnnotatedTargetScanner.loadMethodsFromClass(clazz, RequestMapping.class)
                .forEach(method -> putHandlerExecution(method, instance));
    }

    private void putHandlerExecution(final Method method, final Object instance) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        getRequestMethods(requestMapping)
                .forEach(requestMethod ->
                        {
                            HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
                            HandlerExecutions executions = handlerExecutions.getOrDefault(key, new HandlerExecutions());
                            executions.add(new HandlerExecution(key, method, instance));

                            handlerExecutions.put(key, executions);
                        }
                );
    }

    private List<RequestMethod> getRequestMethods(final RequestMapping requestMapping) {
        RequestMethod[] method = requestMapping.method();

        if (method.length != 0) {
            return Arrays.asList(method);
        }

        return Arrays.asList(RequestMethod.values());
    }

    private Object newInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Fail to create new instance of " + clazz.getName());
        }
    }
}
