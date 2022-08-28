package core.mvc.tobe.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleTypeArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.isPrimitiveOrWrapperType();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String parameterName = methodParameter.getParameterName();
        String value = request.getParameter(parameterName);

        Class<?> parameterType = methodParameter.getType();
        return TypeConverter.convert(parameterType, value);
    }
}
