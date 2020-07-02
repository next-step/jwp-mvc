package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public class BasicTypeArgumentResolver implements ArgumentResolver {

    @Override
    public boolean equalsTo(final Class parameterType) {
        return parameterType.equals(String.class) || parameterType.isPrimitive() || !parameterType.isInterface();
    }

    @Override
    public Object getParameterValue(final HttpServletRequest request, final Class parameterType, final String parameterName) throws Exception {
        String parameterValue = request.getParameter(parameterName);
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(parameterValue);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(parameterValue);
        }
        return parameterValue;
    }
}
