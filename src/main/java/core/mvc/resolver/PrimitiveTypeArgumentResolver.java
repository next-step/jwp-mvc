package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;

public class PrimitiveTypeArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.isPrimitive();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        String requestParameter = request.getParameter(parameter.getParameterName());
        Class<?> parameterType = parameter.getParameterType();
        return convertValue(requestParameter, parameterType);
    }

    private Object convertValue(String requestParameter, Class<?> parameterType) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(requestParameter);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(requestParameter);
        }
        if (parameterType.equals(double.class)) {
            return Double.parseDouble(requestParameter);
        }
        if (parameterType.equals(float.class)) {
            return Float.parseFloat(requestParameter);
        }
        if (parameterType.equals(boolean.class)) {
            return Boolean.parseBoolean(requestParameter);
        }
        if (parameterType.equals(byte.class)) {
            return Byte.parseByte(requestParameter);
        }
        if (parameterType.equals(short.class)) {
            return Short.parseShort(requestParameter);
        }
        if (parameterType.equals(char.class)) {
            return requestParameter.charAt(0);
        }
        if (parameterType.equals(String.class)) {
            return requestParameter;
        }
        throw new IllegalStateException("Not supported Type");
    }
}
