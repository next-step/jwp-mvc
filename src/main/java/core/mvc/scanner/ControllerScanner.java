package core.mvc.scanner;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;
    private Map<Class<?>, Object> instanceMap;

    public ControllerScanner(Object... param) {
        this.reflections = new Reflections(
                ConfigurationBuilder
                        .build(param)
                        .setScanners(
                                new SubTypesScanner(false),
                                new TypeAnnotationsScanner()
                        )
        );
        init();
    }

    public Object getInstance(Class<?> clazz) {
        return instanceMap.get(clazz);
    }

    private void init() {
        final Set<Class<?>> classes = scanAllControllers();
        this.instanceMap = instantiateControllers(classes);
    }

    private Set<Class<?>> scanAllControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> classes) {
        return classes.stream()
                .map(this::determineNonArgsConstructor)
                .filter(Objects::nonNull)
                .map(this::createInstance)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Object::getClass, o -> o));
    }

    private Constructor<?> determineNonArgsConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        return null;
    }

    private Object createInstance(Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
