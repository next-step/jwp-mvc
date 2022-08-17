package core.mvc.tobe;

import core.annotation.web.PathVariable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public class MethodParameter {

    private static final ParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    private Method method;
    private int parameterIndex;
    private Annotation[] annotations;

    public MethodParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, new Annotation[]{});
    }

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

    public MethodParameter(MethodParameter original) {
        this(original.getMethod(), original.getParameterIndex(), original.annotations);
    }

    public MethodParameter clone() {
        return new MethodParameter(this);
    }

    public Method getMethod() {
        return this.method;
    }

    public int getParameterIndex() {
        return this.parameterIndex;
    }

    public Class<?> getParameterType() {
        return this.method.getParameterTypes()[this.parameterIndex];
    }

    public String getParameterName() {
        return NAME_DISCOVERER.getParameterNames(this.method)[parameterIndex];
    }

    public boolean hasPathVariableAnnotation() {
        return Arrays.stream(annotations)
            .anyMatch(annotation -> annotation.annotationType().equals(PathVariable.class));
    }
}
