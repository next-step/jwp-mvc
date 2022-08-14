package core.mvc.tobe.argumentresolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class PrimitiveTypeMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean resolvable(final Method method, final Parameter parameter) {
        if (parameter == null) {
            return false;
        }
        final Class<?> parameterType = parameter.getType();
        return parameterType.isPrimitive() || String.class == parameterType || isPrimitiveWrapper(parameterType);
    }

    private boolean isPrimitiveWrapper(final Class<?> parameterType) {
        return Integer.class == parameterType || Long.class == parameterType;
    }

    @Override
    public Object resolve(final Method method, final Parameter parameter, final String parameterName, final HttpServletRequest request) {
        final Class<?> parameterType = parameter.getType();
        final String requestParameter = request.getParameter(parameterName);

        if (isBlank(requestParameter)) {
            return getDefaultValue(parameterType);
        }
        if (int.class == parameterType || Integer.class == parameterType) {
            return Integer.parseInt(requestParameter);
        }
        if (long.class == parameterType || Long.class == parameterType) {
            return Long.parseLong(requestParameter);
        }
        return parameter;
    }

    private static boolean isBlank(final String requestParameter) {
        return Objects.isNull(requestParameter) || requestParameter.isBlank();
    }

}
