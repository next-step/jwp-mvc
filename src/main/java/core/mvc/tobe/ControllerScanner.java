package core.mvc.tobe;

import java.util.Set;

import org.reflections.Reflections;

import core.annotation.web.Controller;

public class ControllerScanner {

    public Set<Class<?>> findController(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
