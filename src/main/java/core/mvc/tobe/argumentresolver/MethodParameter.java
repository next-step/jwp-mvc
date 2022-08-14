package core.mvc.tobe.argumentresolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class MethodParameter {

    private final Method method;
    private final Parameter parameter;
    private final String parameterName;

    public MethodParameter(final Method method, final Parameter parameter, final String parameterName) {
        validateNullOrBlank(method, parameter, parameterName);
        this.method = method;
        this.parameter = parameter;
        this.parameterName = parameterName;
    }

    private void validateNullOrBlank(final Method method, final Parameter parameter, final String parameterName) {
        validateNull(method);
        validateNull(parameter);
        validateNullOrBlank(parameterName);
    }

    private void validateNull(final Object field) {
        if (Objects.isNull(field)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNullOrBlank(final String parameterName) {
        if (Objects.isNull(parameterName) || parameterName.isBlank()) {
            throw new IllegalArgumentException();
        }
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

    public String getParameterName() {
        return parameterName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MethodParameter that = (MethodParameter) o;
        return Objects.equals(getMethod(), that.getMethod()) && Objects.equals(getParameter(), that.getParameter()) && Objects.equals(
            getParameterName(), that.getParameterName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMethod(), getParameter(), getParameterName());
    }
}
