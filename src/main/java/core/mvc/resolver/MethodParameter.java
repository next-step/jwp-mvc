package core.mvc.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodParameter {

    private final Method method;
    private final Class<?> type;
    private final Annotation[] annotations;
    private final String parameterName;

    public MethodParameter(Method method,
                           Class<?> type,
                           Annotation[] annotations,
                           String parameterName) {
        this.method = method;
        this.type = type;
        this.annotations = annotations;
        this.parameterName = parameterName;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getType() {
        return type;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public String getParameterName() {
        return parameterName;
    }

    public boolean isString() {
        return type == String.class;
    }

    public boolean isInteger() {
        return type == int.class
                || type == Integer.class;
    }
}
