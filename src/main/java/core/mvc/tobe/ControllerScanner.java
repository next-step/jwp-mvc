package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    public static Map<Class<?>, Object> getControllers(Object[] basePackage) throws IllegalAccessException, InstantiationException {
        Map<Class<?>, Object> controllers = new HashMap<>();
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class, true);
        for (Class<?> clazz : annotated) {
            controllers.put(clazz, clazz.newInstance());
        }
        return controllers;
    }

}
