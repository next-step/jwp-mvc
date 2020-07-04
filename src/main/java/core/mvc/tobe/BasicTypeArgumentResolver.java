package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class BasicTypeArgumentResolver implements ArgumentResolver {

    @Override
    public boolean equalsTo(final Class parameterType, final Method method) {
        return  !PathVariableArgumentResolver.isPathVariable(method)
                && (parameterType.equals(String.class)
                || parameterType.isPrimitive()
                || !parameterType.isInterface());
    }

    @Override
    public Object getParameterValue(final HttpServletRequest request, final HttpServletResponse response, final Class parameterType, final String parameterName, Method method) throws Exception {
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
