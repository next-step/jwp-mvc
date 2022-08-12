package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import java.util.Set;

public class ControllerScanner {
    private final Object[] basePackage;

    public ControllerScanner(Object[] basePackage) {
        this.basePackage = basePackage;
    }

    public Set<Class<?>> getControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
