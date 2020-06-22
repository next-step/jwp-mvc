package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private Reflections reflections;
    private Map<Class<?>, Object> controllers = new HashMap<>();


    public void initiateControllers(Object... basePackage) throws IllegalAccessException, InstantiationException {
        reflections = new Reflections(basePackage);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class, true);
        for (Class<?> clazz : annotated) {
            controllers.put(clazz, clazz.newInstance());
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }

}
