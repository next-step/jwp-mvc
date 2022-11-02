package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();

        return (parameterType.equals(HttpServletRequest.class) || parameterType.equals(HttpServletResponse.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> parameterType = parameter.getParameterType();

        if(parameterType.equals(HttpServletRequest.class))
            return request;
        else {
            return response;
        }
    }
}
