package core.mvc.param;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parameters {
    private final static ParameterNameDiscoverer NAME_DISCOVERER =
            new LocalVariableTableParameterNameDiscoverer();

    private final List<Parameter> parameters;

    public Parameters(final Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = NAME_DISCOVERER.getParameterNames(method);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        parameters = IntStream.range(0, parameterTypes.length)
                .mapToObj(i -> new Parameter(
                        parameterNames[i],
                        parameterTypes[i],
                        parameterAnnotations[i].length == 0 ?
                                null :
                                parameterAnnotations[i][0].annotationType()
                        )
                ).collect(Collectors.toList());
    }

    public Object[] extractValues(final HttpServletRequest request) {
        return parameters.stream()
                .map(parameter -> parameter.extractValue(request))
                .toArray();
    }

    public boolean isParamsExist(final HttpServletRequest request) {
        return parameters.stream()
                .allMatch(parameter -> parameter.isParamExist(request));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameters that = (Parameters) o;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters);
    }
}
