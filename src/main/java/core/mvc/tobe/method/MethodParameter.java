package core.mvc.tobe.method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;

public class MethodParameter {

    private static final ParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    private final Method method;
    private final Parameter parameter;

    private MethodParameter(Method method, Parameter parameter) {
        Assert.notNull(method, "'method' must not be null");
        Assert.notNull(parameter, "'parameter' must not be null");
        Assert.isTrue(List.of(method.getParameters()).contains(parameter),
                String.format("parameter(%s) must be included in method(%s)", parameter, method));
        this.method = method;
        this.parameter = parameter;
    }

    public static MethodParameter of(Method method, Parameter parameter) {
        return new MethodParameter(method, parameter);
    }

    public String name() {
        return NAME_DISCOVERER.getParameterNames(method)[parameterIndex()];
    }

    public Class<?> type() {
        return parameter.getType();
    }

    public boolean hasParameterAnnotation(Class<? extends Annotation> annotation) {
        return parameter.isAnnotationPresent(annotation);
    }

    public <T extends Annotation> T parameterAnnotation(Class<T> requestParamClass) {
        return parameter.getDeclaredAnnotation(requestParamClass);
    }

    public boolean hasMethodAnnotation(Class<? extends Annotation> annotation) {
        return method.isAnnotationPresent(annotation);
    }

    public <T extends Annotation> T methodAnnotation(Class<T> annotation) {
        return method.getDeclaredAnnotation(annotation);
    }

    public boolean doesNotHaveAnnotations() {
        return parameter.getDeclaredAnnotations().length == 0;
    }

    private int parameterIndex() {
        return List.of(method.getParameters())
                .indexOf(parameter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, parameter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodParameter that = (MethodParameter) o;
        return Objects.equals(method, that.method) && Objects.equals(parameter, that.parameter);
    }

    @Override
    public String toString() {
        return "MethodParameter{" +
                "method=" + method +
                ", parameter=" + parameter +
                '}';
    }
}
