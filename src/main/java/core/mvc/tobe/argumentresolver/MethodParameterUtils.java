package core.mvc.tobe.argumentresolver;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class MethodParameterUtils {
    public static final ParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    public static Object[] getParameters(HttpServletRequest request, Method method){
        String[] parameterNames = NAME_DISCOVERER.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];

        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            values[i] = request.getParameter(parameterName);
        }

        return values;
    }
}
