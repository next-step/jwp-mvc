package core.mvc.tobe;
import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.Set;

public class ControllerScanner {

    Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    Set<Class<?>> getControllerTypes() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
