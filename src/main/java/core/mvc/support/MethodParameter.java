package core.mvc.support;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MethodParameter {
    private final String name;
    private final Class<?> type;
    private final List<Annotation> annotations;

    public MethodParameter(String name, Class<?> type, List<Annotation> annotation) {
        this.name = name;
        this.type = type;
        this.annotations = Collections.unmodifiableList(annotation);
    }

    public boolean matchClass(Class<?> clazz) {
        return type.equals(clazz);
    }

    public boolean anyMatchClass(Class<?>... classes) {
        return Arrays.stream(classes)
                .anyMatch(this::matchClass);
    }

    public String getName() {
        return name;
    }

    public Annotation getAnnotation(Class<?> clazz) {
        return annotations.stream()
                .filter(a -> a.annotationType().equals(clazz))
                .findFirst()
                .orElse(null);
    }

    public boolean isAnnotationType(Class<?> clazz) {
        return annotations.stream()
                .anyMatch(a -> a.annotationType().equals(clazz));
    }

}
