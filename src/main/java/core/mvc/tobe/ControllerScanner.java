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

    private ControllerScanner() {
        throw new AssertionError();
    }

    public static Set<Object> getControllers(final Reflections reflections) {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<String, Object> controllers = new HashMap<>();

        for (Class<?> clazz : classes) {
            final String name = ControllerName.generate(clazz);
            final Object controller = instantiateController(clazz);

            validateDuplicatedController(controllers, name);

            controllers.put(name, controller);
            logger.debug("Controller {} is instantiated", name);
        }

        return new HashSet<>(controllers.values());
    }

    private static Object instantiateController(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InvalidInstanceException(e);
        }
    }

    private static void validateDuplicatedController(final Map<String, Object> controllers, final String name) {
        if (controllers.containsKey(name)) {
            throw new DuplicatedControllerDefinitionException(name);
        }
    }
}
