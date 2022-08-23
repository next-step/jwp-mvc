package core.mvc.tobe.resolver.method;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

public class MethodParameter {

    private final Method method;
    private final int paramIndex;
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();


    public MethodParameter(Method method, int paramIndex) {
        this.method = method;
        this.paramIndex = paramIndex;
    }

    public Method getMethod() {
        return method;
    }

    public String getParameterName() {
        return Objects.requireNonNull(parameterNameDiscoverer.getParameterNames(method))[paramIndex];
    }

    public Class<?> getParameterType() {
        return method.getParameterTypes()[paramIndex];
    }

    public String pathVariableValue() {
        return method.getAnnotation(RequestMapping.class)
                .value();
    }

    public boolean hasAnnotationWithPathVariable() {
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            return Arrays.stream(parameter.getAnnotations())
                    .anyMatch(annotation -> annotation.annotationType().equals(PathVariable.class));
        }

        return false;
    }

    public boolean isPrimitiveType() {
        return Arrays.stream(method.getParameterTypes())
                .anyMatch(param -> param.equals(int.class) || param.equals(long.class) || param.equals(String.class));
    }

    public String[] getParameterNames(Constructor constructor) {
        return parameterNameDiscoverer.getParameterNames(constructor);
    }

    public boolean hasParameterAnnotations() {
        return Arrays.stream(method.getParameters())
                .anyMatch(parameter -> parameter.getAnnotations().length > 0);
    }
}
