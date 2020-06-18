package core.mvc.tobe.resolver;

import lombok.Getter;
import next.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

@Getter
public class MethodParameter {
    private String name;
    private Parameter parameter;
    private Method method;
    private Map<String, String> pathVariables;

    public static MethodParameter from(String name, Parameter parameter, Method method, Map<String, String> pathVariables) {
        if (StringUtils.isEmpty(name) ||
            Objects.isNull(parameter) ||
            Objects.isNull(method) ||
            Objects.isNull(pathVariables)) {
            throw new IllegalArgumentException();
        }

        return new MethodParameter(
            name,
            parameter,
            method,
            pathVariables
        );
    }

    private MethodParameter(
        String name,
        Parameter parameter,
        Method method,
        Map<String, String> pathVariables
    ) {
        this.method = method;
        this.name = name;
        this.parameter = parameter;
        this.pathVariables = pathVariables;
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return parameter.isAnnotationPresent(annotationType);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return parameter.getAnnotation(annotationType);
    }

    public String getPathVariableValue(String name) {
        return pathVariables.get(name);
    }
}
