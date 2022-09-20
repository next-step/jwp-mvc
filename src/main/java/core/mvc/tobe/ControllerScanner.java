package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);
    private Reflections reflections;

    private final Map<Class<?>, Object> controllers = new HashMap<>();

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> annotatedClass = reflections.getTypesAnnotatedWith(Controller.class);
        try {
            return instantiateControllers(annotatedClass);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.error("Exception : {}", e.getMessage());
            return controllers;
        }
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> annotatedClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<?> aClass : annotatedClass) {
            controllers.put(aClass, aClass.getDeclaredConstructor().newInstance());
        }
        return controllers;
    }
}
