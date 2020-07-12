package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.exception.ReflectionsException;
import core.mvc.tobe.resolver.*;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.springframework.web.util.pattern.PathPattern;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class ControllerScanner {
    private static final Logger logger = getLogger(ControllerScanner.class);

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers = Maps.newHashMap();
    private final HandlerMethodArgumentResolvers handlerMethodArgumentResolvers = HandlerMethodArgumentResolvers.getDefaultHandlerMethodArgumentResolvers();

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<HandlerKey, HandlerExecution> scan() throws ReflectionsException {
        Set<Class<?>> controllerClasses = this.reflections.getTypesAnnotatedWith(Controller.class);
        register(controllerClasses);

        return getHandlers();
    }

    private void register(Set<Class<?>> controllerClasses) throws ReflectionsException {
        for (Class<?> clazz : controllerClasses) {
            try {
                controllers.put(clazz, clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ReflectionsException("unable to load controller.", e);
            }
        }
    }

    private Map<HandlerKey, HandlerExecution> getHandlers() {
        Map<HandlerKey, HandlerExecution> handlers = new HashMap<>();
        for (Method method : getAllMethod()) {
            handlers.put(createHandlerKey(method), createHandlerExecution(method));
        }
        return handlers;
    }

    private Set<Method> getAllMethod() {
        Set<Method> methodsWithRequestMapping = new HashSet<>();
        for (Class<?> controllerClass : this.controllers.keySet()) {
            methodsWithRequestMapping.addAll(ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return methodsWithRequestMapping;
    }

    private HandlerKey createHandlerKey(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String path = annotation.value();
        RequestMethod httpMethod = annotation.method();
        return new HandlerKey(path, httpMethod);
    }

    private HandlerExecution createHandlerExecution(Method method) {
        return new HandlerExecution(method, this.controllers.get(method.getDeclaringClass()), handlerMethodArgumentResolvers);
    }

    public int size() {
        return this.controllers.size();
    }
}
