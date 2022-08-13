package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableMap;

public class ControllerScanner {
    private static final Class<Controller> ANNOTATION_CLASS_FOR_CONTROLLER = Controller.class;

    private final Reflections reflections;
    private Map<Class<?>, Object> controllers;

    public ControllerScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public void initialize() {
        controllers = getControllerClasses().stream()
                .collect(toUnmodifiableMap(
                        Function.identity(),
                        this::getInvokerObjectWithDefaultConstructor
                        )
                );
    }

    private Set<Class<?>> getControllerClasses() {
        return reflections.getTypesAnnotatedWith(ANNOTATION_CLASS_FOR_CONTROLLER);
    }

    private Object getInvokerObjectWithDefaultConstructor(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Controller 인스턴스 생성중 예외 발생", e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return controllers;
    }
}
