package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {

    private Method method;
    private Parameter parameter;

    public MethodParameter(Method method,
                           Parameter parameter) {
        this.method = method;
        this.parameter = parameter;
    }

    public Class<?> getParameterType() {
        return parameter.getType();
    }

    public String getParameterName() {
        return parameter.getName();
    }

    public boolean hasParameterAnnotation() {
        Annotation[] annotations = parameter.getAnnotations();
        return annotations.length > 0;
    }
    public boolean hasParameterAnnotation(Class<? extends Annotation> annotationClass) {
        return parameter.isAnnotationPresent(annotationClass);
    }

    public <T extends Annotation> T getMethodAnnotation(Class<T> annotationClass) {
        return method.getDeclaredAnnotation(annotationClass);
    }

    public <T extends Annotation> T getParameterAnnotation(Class<T> annotationClass) {
        return parameter.getDeclaredAnnotation(annotationClass);
    }

}
