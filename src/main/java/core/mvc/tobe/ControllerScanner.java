package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.Set;

public class ControllerScanner {
    private Reflections reflections;

    public ControllerScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> getAnnotatedControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
