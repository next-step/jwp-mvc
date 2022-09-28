package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {

    private final Method method;
    private final String parameterName;
    private final Parameter parameter;

    public Method getMethod() {
        return method;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Class<?> getParameterType() {
        return parameter.getType();
    }

    public Annotation[] getAnnotations() {
        return parameter.getAnnotations();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public MethodParameter(Method method, String parameterName, Parameter parameter) {
        this.method = method;
        this.parameterName = parameterName;
        this.parameter = parameter;
    }
}
