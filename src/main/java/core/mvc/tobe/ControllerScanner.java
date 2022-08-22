package core.mvc.tobe;

import core.annotation.web.Controller;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private Reflections reflections;

    public ControllerScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class)
            .stream()
            .map(ControllerScanner::instantiateController)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Object::getClass, o -> o));
    }

    private static Object instantiateController(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage(), e);
            return null;
        }
    }

}
