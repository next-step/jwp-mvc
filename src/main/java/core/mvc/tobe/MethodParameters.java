package core.mvc.tobe;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;


import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MethodParameters {

    private final List<MethodParameter> parameters;

    private MethodParameters(Method method, String[] parameterNames, Parameter[] parameters) {
        this.parameters = IntStream.range(0, parameters.length)
                .mapToObj(i -> new MethodParameter(method, parameterNames[i], parameters[i].getType(), parameters[i].getAnnotations()))
                .collect(Collectors.toList());
    }

    public static MethodParameters from(Method method) {
        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        return new MethodParameters(method, parameterNames, parameters);
    }

    public List<MethodParameter> getParameters() {
        return parameters;
    }
}
