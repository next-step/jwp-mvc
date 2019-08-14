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

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return instantiate(controllers);
    }

    private Map<Class<?>, Object> instantiate(Set<Class<?>> controllers) {
        Map<Class<?>, Object> instantiatedControllers = new HashMap<>();
        for (Class<?> controller : controllers) {
            try {
                instantiatedControllers.put(controller, controller.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("Exception : {}", e.getMessage());
            }
        }

        return instantiatedControllers;
    }
}
