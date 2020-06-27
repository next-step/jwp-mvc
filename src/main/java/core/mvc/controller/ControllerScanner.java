package core.mvc.controller;

import core.annotation.web.Controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

public class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class, Object> controllers = new HashMap<>();

    public ControllerScanner(String... basePackage) {
        this.reflections = new Reflections(
            basePackage,
            new MethodAnnotationsScanner(),
            new TypeAnnotationsScanner(),
            new SubTypesScanner()
        );
        initialize();
    }

    public List<Object> getControllers() {
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
