package core.di.factory;


import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ParameterNameDiscoverUtils {

    private final static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public static ConstructorParameters toConstructorParameters(Constructor<?> constructor) {
        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
        Parameter[] parameters = constructor.getParameters();

        return new ConstructorParameters(constructor, getParameterTypeNames(parameterNames, parameters));
    }
    
    public static List<MethodParameter> toMethodParameters(Method method) {
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        return getParameterTypeNames(parameterNames, parameters).stream()
                .map(parameterTypeName -> new MethodParameter(method, parameterTypeName))
                .collect(toList());
    }
    
    private static List<ParameterTypeName> getParameterTypeNames(String[] parameterNames, Parameter[] parameters) {
        return IntStream.range(0, parameterNames.length)
                .mapToObj(i -> new ParameterTypeName(parameterNames[i], parameters[i]))
                .collect(toList());
    }




}
