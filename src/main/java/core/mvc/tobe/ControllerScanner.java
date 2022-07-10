package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(String basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class, Object> getControllers() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        Map<Class, Object> controllerMap = new HashMap<>();
        for (Class<?> controller : controllers) {
            try {
                controllerMap.put(controller, controller.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return controllerMap;
    }
}
