package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class ControllerScanner {

    private Reflections reflections;

    ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return createInstance(controllers);
    }

    private Map<Class<?>, Object> createInstance(Set<Class<?>> controllers) {
        Map<Class<?>, Object> instances = new HashMap<>();
        for (Class<?> controller : controllers) {
            try {
                instances.put(controller, controller.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new InitializedException(e);
            }
        }
        return instances;
    }
}