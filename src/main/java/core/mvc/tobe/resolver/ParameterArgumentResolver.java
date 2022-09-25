package core.mvc.tobe.resolver;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class ParameterArgumentResolver implements ArgumentResolver {

    private static final ParameterArgumentResolver parameterArgumentResolver = new ParameterArgumentResolver();

    private ParameterArgumentResolver() {}

    public static ParameterArgumentResolver getInstance() {
        return parameterArgumentResolver;
    }

    @Override
    public Object[] resolve(HttpServletRequest request, HttpServletResponse response, Method method) {
        String[] parameterNames = ((ParameterNameDiscoverer) new LocalVariableTableParameterNameDiscoverer()).getParameterNames(method);
        return Arrays.stream(Objects.requireNonNull(parameterNames), 0, method.getParameterTypes().length)
                .filter(parameterName -> isParameterContains(request, parameterName))
                .map(parameterName -> parameterValue(request, parameterName))
                .toArray();
    }

    private boolean isParameterContains(HttpServletRequest request, String parameterName) {
        return request.getParameter(parameterName) != null;
    }

    private String parameterValue(HttpServletRequest request, String parameterName) {
        return request.getParameter(parameterName);
    }
}
