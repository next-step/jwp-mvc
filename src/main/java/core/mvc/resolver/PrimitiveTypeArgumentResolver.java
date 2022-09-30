package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrimitiveTypeArgumentResolver implements MethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();

        return (parameterType.isPrimitive() || parameterType.equals(String.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> parameterType = parameter.getParameterType();
        String parameterName = parameter.getParameterName();
        String object = request.getParameter(parameterName);

        if(parameterType.equals(int.class))
            return Integer.parseInt(object);
        else if(parameterType.equals(double.class))
            return Double.parseDouble(object);
        else if(parameterType.equals(long.class))
            return Long.parseLong(object);
        else if(parameterType.equals(float.class))
            return Float.parseFloat(object);

        return object;
    }
}
