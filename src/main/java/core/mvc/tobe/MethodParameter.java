package core.mvc.tobe;

import java.lang.reflect.Method;

public class MethodParameter {

    private final Class<?> type;
    private final Method method;

    public MethodParameter(Class<?> type, Method method) {
        this.type = type;
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public Method getMethod() {
        return method;
    }
}
