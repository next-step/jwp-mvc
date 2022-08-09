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

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getController() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiateControllers(controllerClasses);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> controllers = new HashMap<>();

        try {
            for (Class<?> controllerClass : controllerClasses) {
                controllers.put(controllerClass, controllerClass.newInstance());
            }
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Fail instantiateControllers : {}", e.getMessage());
        }

        return controllers;
    }


}
