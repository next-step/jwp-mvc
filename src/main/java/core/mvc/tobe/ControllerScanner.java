package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ControllerScanner {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Reflections reflections;

    public ControllerScanner(Object[] scanPackage) {
        this.reflections = new Reflections(scanPackage);
    }

    public Set<Class<?>> getControllers() {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Map<Class<?>, Object> getInstantiateControllerMap() {
        Map<Class<?>, Object> controllersMap = new HashMap<>();
        for (Class<?> aClass : getControllers()) {
            Optional<Object> objectOptional = getInstance(aClass);
            objectOptional.ifPresent(instance -> controllersMap.put(aClass, instance));
        }

        return Collections.unmodifiableMap(controllersMap);
    }

    private Optional<Object> getInstance(Class<?> aClass) {
        try {
            return Optional.of(aClass.getDeclaredConstructor().newInstance());
        } catch (ReflectiveOperationException e) {
            logger.error(e.getMessage());
        }

        return Optional.empty();
    }
}
