package core.mvc.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class MethodParameter {

    private final Method method;
    private final Parameter parameter;

    public MethodParameter(Method method, Parameter parameter) {
        this.method = method;
        this.parameter = parameter;
    }

    public Method getMethod() {
        return method;
    }

    public Parameter getParameter() {
        return parameter;
    }


    public Class<?> getParameterType() {
        return parameter.getType();
    }

    public boolean hasParameterAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return parameter.isAnnotationPresent(annotationClass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodParameter that = (MethodParameter) o;
        return Objects.equals(method, that.method) && Objects.equals(parameter, that.parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, parameter);
    }
}
