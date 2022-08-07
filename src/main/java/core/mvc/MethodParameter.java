package core.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {
    private final Method method;
    private final Parameter parameter;

    public MethodParameter(Method method, Parameter parameter) {
        this.method = method;
        this.parameter = parameter;
    }

    public static MethodParameter of(Method method, int idx) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length <= idx) {
            throw new IllegalArgumentException();
        }

        return new MethodParameter(method, parameters[idx]);
    }

    public boolean hasParameterAnnotations() {
        Annotation[] annoArr = parameter.getDeclaredAnnotations();

        return annoArr.length > 0;
    }

    public Class<?> getParameterType() {
        return parameter.getType();
    }

    public boolean hasParameterAnnotation(Class<? extends Annotation> annotation) {
        return parameter.isAnnotationPresent(annotation);
    }

    public <T extends Annotation> T getMethodAnnotation(Class<T> annotation) {
        return method.getDeclaredAnnotation(annotation);
    }

    public <T extends Annotation> T getParameterAnnotation(Class<T> annotation) {
        return parameter.getDeclaredAnnotation(annotation);
    }
}
