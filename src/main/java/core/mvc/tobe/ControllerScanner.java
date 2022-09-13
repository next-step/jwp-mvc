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
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        try {
            return initiateControllers(classes);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            log.error("fail to initiate controllers: {}", e.getMessage());
        }
        throw new IllegalArgumentException("fail to get controllers");
    }

    private Map<Class<?>, Object> initiateControllers(Set<Class<?>> classes) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<Class<?>, Object> controllers = new HashMap();
        for (Class<?> clazz: classes) {
            final Object instance = clazz.getConstructor().newInstance();
            controllers.put(clazz, instance);
        }
        log.info("initiated controllers: {}", controllers);
        return controllers;
    }
}
