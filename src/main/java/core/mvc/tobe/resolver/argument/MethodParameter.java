package core.mvc.tobe.resolver.argument;

import core.annotation.web.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {

    private Method method;

    private String parameterName;

    private Class<?> parameterType;

    private Parameter parameter;

    public MethodParameter(Method method, String parameterName, Parameter parameter) {
        this.method = method;
        this.parameterName = parameterName;
        this.parameter = parameter;
        this.parameterType = parameter.getType();
    }

    public Object getParameterTypeValue(String parameterValue, Class<?> parameterType) {
        Object value = parameterValue;
        if (parameterType.equals(int.class)) {
            value = Integer.parseInt(parameterValue);
        }
        if (parameterType.equals(long.class)) {
            value = Long.parseLong(parameterValue);
        }

        return value;
    }

    public Object getParameterTypeValue(String parameterValue) {
        return getParameterTypeValue(parameterValue, parameterType);
    }

    public String getParameterName() {
        return parameterName;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getMethodRequestMappingValue() {
        return method.getAnnotation(RequestMapping.class).value();
    }


    @Override
    public String toString() {
        return "MethodParameter{" +
                "method=" + method +
                ", parameterName='" + parameterName + '\'' +
                ", parameterType=" + parameterType +
                ", parameter=" + parameter +
                '}';
    }

}
