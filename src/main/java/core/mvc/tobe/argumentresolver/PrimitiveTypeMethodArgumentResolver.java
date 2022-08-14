package core.mvc.tobe.argumentresolver;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class PrimitiveTypeMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean resolvable(final MethodParameter methodParameter) {
        final Class<?> parameterType = methodParameter.getParameterType();
        return parameterType.isPrimitive() || String.class == parameterType || isPrimitiveWrapper(parameterType);
    }

    private boolean isPrimitiveWrapper(final Class<?> parameterType) {
        return Integer.class == parameterType || Long.class == parameterType;
    }

    @Override
    public Object resolve(final MethodParameter methodParameter, final HttpServletRequest request) {
        final Class<?> parameterType = methodParameter.getParameterType();
        final String requestParameter = request.getParameter(methodParameter.getParameterName());

        if (isBlank(requestParameter)) {
            return getDefaultValue(parameterType);
        }
        if (int.class == parameterType || Integer.class == parameterType) {
            return Integer.parseInt(requestParameter);
        }
        if (long.class == parameterType || Long.class == parameterType) {
            return Long.parseLong(requestParameter);
        }
        return requestParameter;
    }

    private static boolean isBlank(final String requestParameter) {
        return Objects.isNull(requestParameter) || requestParameter.isBlank();
    }
}
