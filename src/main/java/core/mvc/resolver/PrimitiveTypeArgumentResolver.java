package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;

public class PrimitiveTypeArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PrimitiveType.isPrimitive(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        String requestParameter = request.getParameter(parameter.getParameterName());
        Class<?> parameterType = parameter.getParameterType();
        return PrimitiveType.convert(parameterType, requestParameter);
    }
}
