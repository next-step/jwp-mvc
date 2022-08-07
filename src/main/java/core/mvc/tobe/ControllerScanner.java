package core.mvc.tobe;

import core.annotation.web.Controller;
import core.mvc.tobe.exception.DuplicatedControllerDefinitionException;
import core.mvc.tobe.exception.InvalidInstanceException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private final Map<String, Object> controllers = new HashMap<>();
    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public void instantiateControllers() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : classes) {
            final String name = ControllerName.generate(clazz);
            final Object controller = instantiateController(clazz);

            validateDuplicatedController(name);

            controllers.put(name, controller);
            logger.debug("Controller {} is instantiated", name);
        }
    }

    private Object instantiateController(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InvalidInstanceException(e);
        }
    }

    private void validateDuplicatedController(final String name) {
        if (controllers.containsKey(name)) {
            throw new DuplicatedControllerDefinitionException(name);
        }
    }

    public Set<Object> getControllers() {
        return new HashSet<>(controllers.values());
    }
}
