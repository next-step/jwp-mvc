package core.mvc.tobe;

import core.annotation.web.Controller;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    private ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public ControllerScanner(Object[] basePackage) {
        this(new Reflections(basePackage));
    }

    public Set<Class<?>> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }
}
