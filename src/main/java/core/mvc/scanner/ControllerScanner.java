package core.mvc.scanner;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.mvc.support.exception.FailedNewInstanceException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class ControllerScanner implements Scanner {
    private final Map<Class<?>, Object> classInstances = Maps.newHashMap();

    public ControllerScanner() {}

    @Override
    public void scan(Object[] basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class, true);

        for (Class<?> controller : controllers) {
            classInstances.put(controller, newInstance(controller));
        }
    }

    private Object newInstance(Class<?> controllerClass) {
        try {
            final Constructor<?> constructor = controllerClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FailedNewInstanceException(controllerClass, e);
        }
    }

    @Override
    public Set<Class<?>> getScannedClasses() {
        return classInstances.keySet();
    }

    @Override
    public Object getInstance(Class<?> clazz) {
        return classInstances.get(clazz);
    }

}
