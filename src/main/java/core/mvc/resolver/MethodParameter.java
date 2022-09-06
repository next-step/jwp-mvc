package core.mvc.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodParameter {
    private final Method method;
    private final Class<?> parameterType;
    private final String parameterName;
    private final Annotation[] annotations;

    public MethodParameter(Method method, Class<?> parameterType, String parameterName, Annotation[] annotations) {
        this.method = method;
        this.parameterType = parameterType;
        this.parameterName = parameterName;
        this.annotations = annotations;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }
}
