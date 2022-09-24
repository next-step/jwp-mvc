package core.mvc.tobe;

import core.annotation.web.PathVariable;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class ParameterArgumentResolver implements ArgumentResolver {
    private final HttpServletRequest request;
    private final Class<?>[] parameterTypes;
    private final String[] parameterNames;
    public ParameterArgumentResolver(HttpServletRequest request, Method method) {
        this.request = request;
        this.parameterTypes = method.getParameterTypes();
        this.parameterNames = ((ParameterNameDiscoverer) new LocalVariableTableParameterNameDiscoverer())
                .getParameterNames(method);
    }

    @Override
    public Object[] resolve() {
        // 조건 필요
        return IntStream.range(0, parameterTypes.length)
                .filter(this::isParameterContains)
                .mapToObj(this::parameterValue)
                .toArray();
    }


    private boolean isParameterContains(int index) {
        return request.getParameter(parameterNames[index]) != null;
    }

    private String parameterValue(int index) {
        return request.getParameter(parameterNames[index]);
    }
}
