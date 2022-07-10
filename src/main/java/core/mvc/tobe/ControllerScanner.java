package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControllerScanner {
    private final Reflections reflections;
    private final Map<Class<?>, Object> controllerMap;

    public ControllerScanner(Reflections reflections) {
        this.reflections = reflections;
        this.controllerMap = createControllerMap();
    }

    public Map<Class<?>, Object> createControllerMap() {
        return reflections.getTypesAnnotatedWith(Controller.class).stream().map(clazz -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toMap(Object::getClass, Function.identity(), (o1, o2) -> o1));
    }

    public Map<Class<?>, Object> getControllerMap() {
        return controllerMap;
    }
}
