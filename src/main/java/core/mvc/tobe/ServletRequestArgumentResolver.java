package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRequestArgumentResolver implements ArgumentResolver{
    @Override
    public boolean equalsTo(final Class parameterType) {
        return parameterType.isInterface() && parameterType.equals(HttpServletRequest.class);
    }

    @Override
    public Object getParameterValue(final HttpServletRequest request, final HttpServletResponse response, final Class parameterType, final String parameterName) throws Exception {
        return null;
    }
}
