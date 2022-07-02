package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
        init();
    }

    public Set<Method> getMethodsWithRequestMapping(Class<?> clazz) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void init() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        classes.forEach(clazz -> {
            try {
                controllers.put(clazz, clazz.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
