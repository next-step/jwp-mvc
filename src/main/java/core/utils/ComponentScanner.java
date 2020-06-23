package core.utils;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    private Reflections reflections;

    public ComponentScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> scanClassesBy(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation, true);
    }

    public List<Method> scanMethodsBy(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }
}
