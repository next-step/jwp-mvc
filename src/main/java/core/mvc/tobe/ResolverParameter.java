package core.mvc.tobe;

import java.lang.reflect.Method;

public class ResolverParameter {

    private Method method;
    private Class type;
    private String parameterName;

    public ResolverParameter(final Method method, final Class type, final String parameterName) {
        this.method = method;
        this.type = type;
        this.parameterName = parameterName;
    }

    public Method getMethod() {
        return method;
    }

    public Class getType() {
        return type;
    }

    public String getParameterName() {
        return parameterName;
    }
}
