package core.mvc.tobe;

import java.util.Set;

import org.reflections.Reflections;

import core.annotation.web.Controller;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> findControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
