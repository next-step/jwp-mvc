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
    private Map<Class<?>, Object> controllers;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
        this.controllers = new HashMap<>();
        initialize();
    }

    private void initialize() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.forEach(this::putController);
    }

    private void putController(Class<?> controller) {
        try {
            this.controllers.put(controller, controller.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("instantiate controller failed.", e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
