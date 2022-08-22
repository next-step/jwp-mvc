package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import core.annotation.web.PathVariable;

public class MethodParameter {

    private static final ParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    private Method method;
    private int parameterIndex;
    private Annotation[] annotations;

    public MethodParameter(Method method, int parameterIndex, Annotation[] annotations) {
        validate(method, parameterIndex);

        this.method = method;
        this.parameterIndex = parameterIndex;
        this.annotations = annotations;
    }

    private void validate(Method method, int parameterIndex) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length <= parameterIndex) {
            throw new IllegalArgumentException();
        }
    }

    public Method getMethod() {
        return this.method;
    }

    public Class<?> getParameterType() {
        return this.method.getParameterTypes()[this.parameterIndex];
    }

    public String getParameterName() {
        return Objects.requireNonNull(NAME_DISCOVERER.getParameterNames(this.method))[parameterIndex];
    }

    public boolean hasPathVariableAnnotation() {
        return Arrays.stream(annotations)
            .anyMatch(annotation -> annotation.annotationType().equals(PathVariable.class));
    }
}
