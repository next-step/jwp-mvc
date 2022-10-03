package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PrimitiveTypeArgumentResolver implements MethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();

        /**
         * 예를 들어 PathVariable 어노테이션이 있으면 PathVariableArgumentResolver 가 호출되어야한다.
         * 그렇기 때문에 매개변수에 어노테이션이 없는 것들만 true 가 되야함.
         */
        if(parameter.getAnnotations().length > 0)
            return false;

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
