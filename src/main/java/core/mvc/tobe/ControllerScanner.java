package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.*;

public class ControllerScanner {
    private final Reflections reflections;
    private final Map<Class, Object> controllers = new HashMap<>();

    ControllerScanner(String... basePackage) {
        this.reflections = new Reflections(
                basePackage,
                new MethodAnnotationsScanner(),
                new TypeAnnotationsScanner(),
                new SubTypesScanner()
        );
        initialize();
    }

    List<Object> getControllers() {
        return new ArrayList<>(this.controllers.values());
    }

    private void initialize() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.stream().map(clazz -> newInstance(clazz));
        for (Class clazz : controllers) {
            this.controllers.put(clazz, newInstance(clazz));
        }
    }

    private Object newInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
