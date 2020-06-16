package core.mvc.param;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parameters {
    private final static ParameterNameDiscoverer NAME_DISCOVERER =
            new LocalVariableTableParameterNameDiscoverer();

    private final List<Parameter> parameters;

    public Parameters(final Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = NAME_DISCOVERER.getParameterNames(method);

        parameters = IntStream.range(0, parameterTypes.length)
                .mapToObj(i -> new Parameter(parameterNames[i], parameterTypes[i], null))
                .collect(Collectors.toList());
    }

    public Object[] getValues(final HttpServletRequest request) {
        return parameters.stream()
                .map(parameter -> parameter.extractValue(request))
                .toArray();
    }
}
