package core.mvc.tobe;

import java.lang.reflect.Method;
import java.util.stream.IntStream;
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

        return getParameters(method.getParameterTypes(), parameterNames, request);
    }

    private Object[] getParameters(final Class<?>[] parameterTypes, final String[] parameterNames, final HttpServletRequest request) {
        return IntStream.range(0, parameterNames.length)
            .mapToObj(i -> getValueWithMatchingType(parameterTypes[i], request.getParameter(parameterNames[i])))
            .toArray();
    }

    private Object getValueWithMatchingType(Class<?> parameterType, String parameter) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(parameter);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(parameter);
        }

        return parameter;
    }
}
