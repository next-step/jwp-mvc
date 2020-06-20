package core.mvc.support;

import core.annotation.web.PathVariable;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class MethodParameter {

    private final Method method;
    private final int paramIndex;
    private final Parameter parameter;
    private final String parameterName;
    private final Annotation[] parameterAnnotations;

    public MethodParameter(Method method, int paramIndex, Parameter parameter, String parameterName, @Nullable Annotation[] parameterAnnotations) {
        this.method = method;
        this.paramIndex = paramIndex;
        this.parameter = parameter;
        this.parameterName = parameterName;
        this.parameterAnnotations = parameterAnnotations != null ? parameterAnnotations : new Annotation[0];
    }

    @Override
    public String toString() {
        return "MethodParameter{" +
                "method=" + method +
                ", paramIndex=" + paramIndex +
                ", parameter=" + parameter +
                ", parameterName='" + parameterName + '\'' +
                ", parameterAnnotations=" + Arrays.toString(parameterAnnotations) +
                '}';
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return null;
    }
}
