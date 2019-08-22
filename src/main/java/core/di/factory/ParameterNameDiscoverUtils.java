package core.di.factory;


import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ParameterNameDiscoverUtils {

    private final static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public static List<MethodParameter> toMethodParmeters(Method method) {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        return toMethodParameters(method, parameterNames, parameters);
    }

    private static List<MethodParameter> toMethodParameters(Method method, String[] parameterNames, Parameter[] parameters) {
        return IntStream.range(0, parameterNames.length)
                .mapToObj(i -> new MethodParameter(method, parameterNames[i], parameters[i], i))
                .collect(toList());
    }




}
