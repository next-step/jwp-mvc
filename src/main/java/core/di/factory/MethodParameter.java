package core.di.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter extends ParameterTypeName {

    private final Method method;

    public MethodParameter(Method method, ParameterTypeName parameterTypeName) {
        super(parameterTypeName);
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}
