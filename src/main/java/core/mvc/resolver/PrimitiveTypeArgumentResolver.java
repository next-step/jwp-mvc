package core.mvc.resolver;

import org.apache.commons.lang3.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrimitiveTypeArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return ClassUtils.isPrimitiveOrWrapper(methodParameter.getParameterType())
        || CharSequence.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> parameterType = methodParameter.getParameterType();
        String value = request.getParameter(methodParameter.getParameterName());

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
}
