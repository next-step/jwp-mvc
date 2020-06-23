package core.mvc;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    private final Map<Class<?>, Object> classInstances = Maps.newHashMap();

    public ControllerScanner(Object[] basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);

        for (Class<?> controller : controllers) {
            classInstances.put(controller, newInstance(controller));
        }
    }

    private Object newInstance(Class<?> controllerClass) {
        try {
            Constructor<?> constructor = controllerClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Class<?>> getControllers() {
        return classInstances.keySet();
    }

    public Object getInstance(Class<?> clazz) {
        return classInstances.get(clazz);
    }

}
