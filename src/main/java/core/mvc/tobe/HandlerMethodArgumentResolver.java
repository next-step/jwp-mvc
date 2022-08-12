package core.mvc.tobe;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public class HandlerMethodArgumentResolver {

    private static final ParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    private static final Object[] EMPTY_PARAMETERS = new Object[0];

    public Object[] resolve(final Method method, final HttpServletRequest request) {
        final String[] parameterNames = DISCOVERER.getParameterNames(method);
        if (parameterNames == null) {
            return EMPTY_PARAMETERS;
        }

        final Object[] params = new Object[parameterNames.length];

        for (int i = 0; i < parameterNames.length; i++) {
            final String parameterName = parameterNames[i];
            final String parameter = request.getParameter(parameterName);
            params[i] = parameter;
        }


        return params;
    }
}
