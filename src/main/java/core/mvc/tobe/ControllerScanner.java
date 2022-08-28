package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControllerScanner {
    private Reflections reflections;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getControllers() {
        Set<Class<?>> controllerAnnotatedClass = reflections.getTypesAnnotatedWith(Controller.class);
        return controllerAnnotatedClass.stream().collect(Collectors.toMap(Function.identity(), controllerClass -> {
            try {
                return controllerClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
