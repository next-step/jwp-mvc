package core.mvc.tobe;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MethodParameters {

    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final Map<String, TypeDescriptor> parameterMap = new HashMap<>();

    public MethodParameters(final Method method) {
        final Parameter[] parameters = method.getParameters();
        final String[] parameterNames = nameDiscoverer.getParameterNames(method);

        init(parameters, parameterNames);
    }

    public MethodParameters(final Constructor<?> constructor) {
        final Parameter[] parameters = constructor.getParameters();
        final String[] parameterNames = nameDiscoverer.getParameterNames(constructor);

        init(parameters, parameterNames);
    }

    private void init(Parameter[] parameters, String[] parameterNames) {
        if (parameters == null || parameterNames == null) {
            return;
        }

        for (int i = 0; i < parameters.length; i++) {
            final String parameterName = parameterNames[i];
            final TypeDescriptor parameterType = TypeDescriptor.valueOf(parameters[i].getType());

            parameterMap.put(parameterName, parameterType);
        }
    }

    public TypeDescriptor get(final String key) {
        return parameterMap.get(key);
    }

    public Stream<Map.Entry<String, TypeDescriptor>> stream() {
        return parameterMap.entrySet().stream();
    }

    public boolean contains(Class<?> type) {
        return parameterMap.values().stream()
                .anyMatch(value -> value.equals(type));
    }
}
