package core.mvc.tobe.resolver;



import core.mvc.tobe.resolver.method.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !parameter.hasParameterAnnotations();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> parameterType = methodParameter.getParameterType();

        String value = request.getParameter(methodParameter.getParameterName());

        if (parameterType.equals(HttpServletRequest.class)) {
            return request;
        }

        if (parameterType.equals(HttpServletResponse.class)) {
            return response;
        }

        return getObject(parameterType, value);
    }

    public Object getObject(Class<?> parameterType, String value) {
        if (parameterType.equals(int.class) || parameterType.equals(Integer.class)) {
            return Integer.parseInt(value);
        }

        if (parameterType.equals(long.class) || parameterType.equals(Long.class)) {
            return Long.parseLong(value);
        }

        if (parameterType.equals(Float.class) || parameterType.equals(float.class)) {
            return Float.parseFloat(value);
        }

        if (parameterType.equals(boolean.class) || parameterType.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        }

        return value;
    }
}
