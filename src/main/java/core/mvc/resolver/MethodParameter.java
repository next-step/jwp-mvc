package core.mvc.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class MethodParameter {

    private final Method method;
    private final Parameter parameter;
    private final String parameterName;

    public MethodParameter(Method method, Parameter parameter, String parameterName) {
        this.method = method;
        this.parameter = parameter;
        this.parameterName = parameterName;
    }

    public Method getMethod() {
        return method;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Class<?> getParameterType() {
        return parameter.getType();
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        return parameter.isAnnotationPresent(annotation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodParameter that = (MethodParameter) o;
        return Objects.equals(method, that.method) && Objects.equals(parameter, that.parameter) && Objects.equals(parameterName, that.parameterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, parameter, parameterName);
    }
}
