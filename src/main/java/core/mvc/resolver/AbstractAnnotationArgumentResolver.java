package core.mvc.resolver;

import core.mvc.exception.InvalidParameterAnnotation;
import core.mvc.exception.MethodArgumentTypeNotSupportedException;

import java.util.Arrays;

public abstract class AbstractAnnotationArgumentResolver implements ArgumentResolver {

    public boolean supportAnnotation(MethodParameter methodParameter, Class<?> annotationClass) {
        return Arrays.stream(methodParameter.getAnnotations()).anyMatch(
                annotation -> annotation.annotationType() == annotationClass
        );
    }

    public <T> T getAnnotation(MethodParameter methodParameter, Class<T> annotationClass) {
        return Arrays.stream(methodParameter.getAnnotations())
                .filter(annotation -> annotation.annotationType() == annotationClass)
                .findAny()
                .map(annotationClass::cast)
                .orElseThrow(() -> new InvalidParameterAnnotation(annotationClass));
    }

    public Object resolve(MethodParameter methodParameter, Object argument) {
        if (methodParameter.isString()) {
            return argument;
        }
        if (methodParameter.isInteger()) {
            return Integer.valueOf(argument.toString());
        }

        throw new MethodArgumentTypeNotSupportedException(methodParameter.getType(), argument);
    }
}
