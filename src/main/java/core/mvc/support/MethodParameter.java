package core.mvc.support;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class MethodParameter {
    private final String name;
    private final Class<?> type;
    private final Annotation annotation;

    public MethodParameter(String name, Class<?> type, Annotation annotation) {
        this.name = name;
        this.type = type;
        this.annotation = annotation;
    }

    public boolean matchClass(Class<?> clazz) {
        return type.equals(clazz);
    }

    public boolean anyMatchClass(Class<?>... classes) {
        return Arrays.stream(classes)
                .map(this::matchClass)
                .filter(Boolean::booleanValue)
                .findFirst()
                .orElse(false);
    }

    public String getName() {
        return name;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public boolean isEmptyAnnotation() {
        return annotation == null;
    }

    public boolean isAnnotationType(Class<?> clazz) {
        return annotation.annotationType().equals(clazz);
    }

}
