package core.mvc.tobe.resolver.argument;

import javax.servlet.http.HttpServletRequest;

public class RequestParameterArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean isSupport(Class<?> parameterType) {
        return parameterType.isPrimitive() || parameterType == String.class;
    }

    @Override
    public Object resolve(HttpServletRequest request, String parameterName, Class<?> parameterType) {
        String parameterValue = request.getParameter(parameterName);
        Object value = parameterValue;
        if (parameterType.equals(int.class)) {
            value = Integer.parseInt(parameterValue);
        }
        if (parameterType.equals(long.class)) {
            value = Long.parseLong(parameterValue);
        }

        return value;
    }
}
