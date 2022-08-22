package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        Class<?> parameterType = methodParameter.getParameterType();
        String value = request.getParameter(methodParameter.getParameterName());

        if (parameterType.equals(HttpServletRequest.class)) {
            return request;
        }

        if (parameterType.equals(int.class)) {
            return Integer.parseInt(value);
        }

        if (parameterType.equals(long.class)) {
            return Long.parseLong(value);
        }

        if (parameterType.equals(double.class)) {
            return Double.parseDouble(value);
        }

        if (parameterType.equals(float.class)) {
            return Float.parseFloat(value);
        }

        return value;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return true;
    }
}
