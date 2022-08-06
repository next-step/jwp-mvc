package core.mvc.tobe;

import java.lang.reflect.Method;

public class MethodParameter {

    private final Class<?> type;
    private final Method method;
    private final String parameterName;

    public MethodParameter(Class<?> type, Method method, String parameterName) {
        this.type = type;
        this.method = method;
        this.parameterName = parameterName;
    }

    public Class<?> getType() {
        return type;
    }

    public Method getMethod() {
        return method;
    }

    public String getParameterName() {
        return parameterName;
    }
}
