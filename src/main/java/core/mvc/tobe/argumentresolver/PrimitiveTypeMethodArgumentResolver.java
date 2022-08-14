package core.mvc.tobe.argumentresolver;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class PrimitiveTypeMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean resolvable(final MethodParameter methodParameter) {
        final Class<?> parameterType = methodParameter.getParameterType();
        return PrimitiveParameter.isPrimitive(parameterType);
    }

    @Override
    public Object resolve(final MethodParameter methodParameter, final HttpServletRequest request) {
        final Class<?> parameterType = methodParameter.getParameterType();
        final String requestParameter = request.getParameter(methodParameter.getParameterName());

        if (isBlank(requestParameter)) {
            return getDefaultValue(parameterType);
        }

        final PrimitiveParameter primitiveParameter = PrimitiveParameter.from(parameterType);
        return primitiveParameter.convert(requestParameter);
    }

    private static boolean isBlank(final String requestParameter) {
        return Objects.isNull(requestParameter) || requestParameter.isBlank();
    }
}
