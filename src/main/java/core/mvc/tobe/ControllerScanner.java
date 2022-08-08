package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ControllerScanner {

    public static final Class<Controller> FOUND_CONTROLLER_ANNOTATION_TARGET = Controller.class;
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);
    private static final Class<?>[] EMPTY_CLASS_ARRAY = {};

    private final Reflections reflections;

    private Map<Class<?>, Object> cachedControllers;

    public ControllerScanner(String basePackage) {
        Assert.hasText(basePackage, "'basePackage' must not be blank");
        this.reflections = new Reflections(basePackage);
    }

    public static ControllerScanner from(String basePackage) {
        return new ControllerScanner(basePackage);
    }


    public Map<Class<?>, Object> controllers() {
        if (cachedControllers == null) {
            cachedControllers = newControllers();
        }
        return cachedControllers;
    }

    public Optional<Object> instance(Class<?> clazz) {
        return Optional.ofNullable(controllers().get(clazz));
    }

    private Map<Class<?>, Object> newControllers() {
        return reflections.getTypesAnnotatedWith(FOUND_CONTROLLER_ANNOTATION_TARGET)
                .stream()
                .collect(Collectors.toMap(clazz -> clazz, this::newInstanceFromDefaultConstructor));
    }

    private Object newInstanceFromDefaultConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor(EMPTY_CLASS_ARRAY).newInstance();
        } catch (Exception e) {
            logger.error(String.format("failed instance(%s) creation", clazz), e);
            throw new BeanCreationException(clazz.getName(), String.format("an error occurred during instance(%s) creation", clazz), e);
        }
    }
}
