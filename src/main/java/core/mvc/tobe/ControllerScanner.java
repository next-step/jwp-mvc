package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class ControllerScanner {

    private final Reflections reflections;

    private final Map<Class, Object> controllers;

    public ControllerScanner(String basePackage) {
        this.reflections = new Reflections(basePackage);
        this.controllers = reflections.getTypesAnnotatedWith(Controller.class).stream()
                   .collect(toMap(Function.identity(), controller -> getControllerInstance(controller)));
    }

    public Map<Class, Object> getControllers() {
        return controllers;
    }

    private Object getControllerInstance(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
