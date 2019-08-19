package core.mvc.tobe;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MethodParameters {

    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final Map<String, MethodParameter> parameterMap = new HashMap<>();
    private final Executable executable;
    private final Annotation[] annotations;

    public MethodParameters(final Method method) {
        final Parameter[] parameters = method.getParameters();
        final String[] parameterNames = nameDiscoverer.getParameterNames(method);

        init(parameters, parameterNames);

        this.executable = method;
        this.annotations = method.getAnnotations();
    }

    public MethodParameters(final Constructor<?> constructor) {
        final Parameter[] parameters = constructor.getParameters();
        final String[] parameterNames = nameDiscoverer.getParameterNames(constructor);

        init(parameters, parameterNames);

        this.executable = constructor;
        this.annotations = constructor.getAnnotations();
    }

    private void init(final Parameter[] parameters, final String[] parameterNames) {
        if (parameters == null || parameterNames == null) {
            return;
        }

        for (int i = 0; i < parameters.length; i++) {
            final String parameterName = parameterNames[i];
            final MethodParameter methodParameter = new MethodParameter(parameters[i], parameterName, i);
            parameterMap.put(parameterName, methodParameter);
        }
    }

    public Stream<Map.Entry<String, MethodParameter>> stream() {
        return parameterMap.entrySet().stream();
    }

    public boolean isParameterTypePresent(final Class<?> type) {
        return parameterMap.values().stream()
                .anyMatch(value -> value.equalsType(type));
    }

    public boolean isParameterAnnotationPresent(final Class<? extends Annotation> annotationClass) {
        return parameterMap.values().stream()
                .anyMatch(methodParameter -> methodParameter.isAnnotationPresent(annotationClass));
    }

    public Annotation getAnnotation(final Class<? extends Annotation> annotationClass) {
        return executable.getAnnotation(annotationClass);
    }
}
