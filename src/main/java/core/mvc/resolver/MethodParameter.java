package core.mvc.resolver;

import core.mvc.exception.InvalidParameterAnnotation;
import core.mvc.exception.MethodArgumentTypeNotSupportedException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodParameter {

    private final Method method;
    private final Class<?> type;
    private final Annotation[] annotations;
    private final String parameterName;

    public MethodParameter(Method method,
                           Class<?> type,
                           Annotation[] annotations,
                           String parameterName) {
        this.method = method;
        this.type = type;
        this.annotations = annotations;
        this.parameterName = parameterName;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getType() {
        return type;
    }

    public String getParameterName() {
        return parameterName;
    }

    public boolean supportAnnotation(Class<?> annotationClass) {
        return Arrays.stream(annotations).anyMatch(
                annotation -> annotation.annotationType() == annotationClass
        );
    }

    public <T> T getAnnotation(Class<T> annotationClass) {
        return Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType() == annotationClass)
                .findAny()
                .map(annotationClass::cast)
                .orElseThrow(() -> new InvalidParameterAnnotation(annotationClass));
    }

    public Object resolveArgument(Object argument) {
        if (isString()) {
            return argument;
        }
        if (isInteger()) {
            return Integer.valueOf(argument.toString());
        }

        throw new MethodArgumentTypeNotSupportedException(getType(), argument);
    }

    private boolean isString() {
        return type == String.class;
    }

    private boolean isInteger() {
        return type == int.class
                || type == Integer.class;
    }
}
