package core.mvc.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {
    private final Method method;
    private final Class<?> parameterType;
    private final String parameterName;
    private final Annotation[] annotations;

    public MethodParameter(Method method, Parameter methodParameter, String parameterName) {
        this.method = method;
        this.parameterType = methodParameter.getType();
        this.annotations = methodParameter.getAnnotations();
        this.parameterName = parameterName;
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
