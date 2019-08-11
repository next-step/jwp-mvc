package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.Set;

public class ControllerScanner {
    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
