package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class BasicTypeArgumentResolver implements ArgumentResolver {

    @Override
    public boolean support(final Class parameterType, final Method method) {
        return  !PathVariableArgumentResolver.isPathVariable(method)
                && (parameterType.equals(String.class)
                || parameterType.isPrimitive()
                || !parameterType.isInterface());
    }

    @Override
    public Object getParameterValue(final HttpServletRequest request, final HttpServletResponse response, final ResolverParameter resolverParameter) throws Exception {
        Class parameterType = resolverParameter.getType();
        String parameterValue = request.getParameter(resolverParameter.getParameterName());

        if (parameterType.equals(int.class)) {
            return Integer.parseInt(parameterValue);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(parameterValue);
        }
        return parameterValue;
    }
}
