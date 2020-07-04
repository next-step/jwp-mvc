package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class ServletRequestArgumentResolver implements ArgumentResolver{

    @Override
    public boolean equalsTo(final Class parameterType, final Method method) {
        return parameterType.isInterface() && parameterType.equals(HttpServletRequest.class);
    }

    @Override
    public Object getParameterValue(final HttpServletRequest request, final HttpServletResponse response, final Class parameterType, final String parameterName, Method method) throws Exception {
        return request;
    }
}
