package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        return instantiateControllers(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
        Map<Class<?>, Object> classMap = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            Constructor<?> constructor = controllerClass.getConstructors()[0];

            try {
                classMap.put(controllerClass, constructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                logger.warn("@Controller has been annotated an inappropriate class.");
            }
        }
        return classMap;
    }
}
