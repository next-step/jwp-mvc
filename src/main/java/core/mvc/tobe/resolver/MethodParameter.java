package core.mvc.tobe.resolver;

import lombok.Getter;
import next.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class MethodParameter {
    private String name;
    private Parameter parameter;
    private Class<?> containingClass;
    private Class<?> type;
    private List<Annotation> annotations;

    public static MethodParameter from(String name, Parameter parameter, Method method) {
        if (StringUtils.isEmpty(name) ||
            Objects.isNull(parameter) ||
            Objects.isNull(method)
        ) {
            throw new IllegalArgumentException();
        }

        return new MethodParameter(name, parameter, method.getDeclaringClass(), parameter.getType(), Arrays.asList(parameter.getAnnotations()));
    }

    public MethodParameter(String name, Parameter parameter, Class<?> containingClass, Class<?> type, List<Annotation> annotations) {
        this.name = name;
        this.parameter = parameter;
        this.containingClass = containingClass;
        this.type = type;
        this.annotations = annotations;
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return parameter.isAnnotationPresent(annotationType);
    }
}
