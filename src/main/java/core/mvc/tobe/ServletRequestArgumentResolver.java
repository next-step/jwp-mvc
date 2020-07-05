package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class ServletRequestArgumentResolver implements ArgumentResolver{

    @Override
    public boolean support(final Class parameterType, final Method method) {
        return parameterType.isInterface() && parameterType.equals(HttpServletRequest.class);
    }

    @Override
    public Object getParameterValue(final HttpServletRequest request, final HttpServletResponse response, final ResolverParameter resolverParameter) throws Exception {
        return request;
    }
}
