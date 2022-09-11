package core.mvc.tobe;


import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Object... basePackages) {
        reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllerList() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> instantiateControllerMap = new HashMap<>();
        try {
            for (Class<?> clazz : controllers) {
                instantiateControllerMap.put(clazz, clazz.getDeclaredConstructor().newInstance());
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            logger.error(e.getMessage());
        }
        return instantiateControllerMap;
    }

}
