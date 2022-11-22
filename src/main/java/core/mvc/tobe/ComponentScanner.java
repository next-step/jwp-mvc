package core.mvc.tobe;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    private static final Logger logger = LoggerFactory.getLogger(ComponentScanner.class);
    private final Reflections reflections;

    private ComponentScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public static ComponentScanner of(Object[] basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return new ComponentScanner(reflections);
    }

    public Map<Class<?>, Object> doScan(Class<? extends Annotation> annotation) {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);
        return classes.stream()
                .collect(Collectors.toMap(c -> c, this::newInstance));
    }

    private Object newInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            logger.error("exception ---> {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
