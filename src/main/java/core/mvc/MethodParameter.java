package core.mvc;

import core.annotation.web.PathVariable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class MethodParameter {
    private String name;
    private Class<?> type;
    private Annotation[] annotations;

    private static final List<Class<?>> PRIMITIVE_TYPE;
    private static final List<Class<?>> WRAPPER_TYPE;

    static {
        PRIMITIVE_TYPE = Arrays.asList(int.class, long.class, byte.class, boolean.class);
        WRAPPER_TYPE = Arrays.asList(Integer.class, Long.class, Byte.class, Boolean.class);
    }

    public MethodParameter(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public MethodParameter(String name, Parameter parameter) {
        this.name = name;
        this.type = parameter.getType();
        this.annotations = parameter.getAnnotations();
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isPathVariable() {
        return Arrays.stream(annotations)
                .anyMatch(annotation -> annotation.annotationType() == PathVariable.class);
    }

    public boolean isAnnotationNotExist() {
        return annotations == null || annotations.length == 0;
    }

    public boolean isPrimitiveType() {
        return PRIMITIVE_TYPE.contains(type);
    }

    public boolean isWrapperType() {
        return WRAPPER_TYPE.contains(type);
    }

    public boolean isString() {
        return type.equals(String.class);
    }
}
