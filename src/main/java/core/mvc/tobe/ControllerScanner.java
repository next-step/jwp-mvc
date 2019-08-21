package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> scan() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return createControllers(controllerClasses);
    }

    private Map<Class<?>, Object> createControllers(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllers = new HashMap<>(controllerClasses.size());
        for (Class<?> clazz : controllerClasses) {
            try {
                controllers.put(clazz, clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("failed to instantiate class : {}", clazz.getSimpleName(), e);
            }
        }
        return controllers;
    }
}
