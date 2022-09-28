package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private Map<Class<?>, Object> mapByClass = new HashMap<>();

    public ControllerScanner() throws InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("next.controller");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : classes) {
            // TODO
            Object instance = clazz.newInstance();
            mapByClass.put(clazz, instance);
        }
    }

    public Map<Class<?>, Object> getControllerClass() {
        return mapByClass;
    }
}
