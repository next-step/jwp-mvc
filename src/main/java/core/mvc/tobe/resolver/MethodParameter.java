package core.mvc.tobe.resolver;

import core.annotation.web.RequestMapping;
import core.mvc.tobe.ReflectionUtils;
import lombok.Getter;
import next.util.StringUtils;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static core.mvc.tobe.ReflectionUtils.REQUEST_MAPPING_ANNOTATION_CLASS;

@Getter
public class MethodParameter {
    private String name;
    private Parameter parameter;
    private Class<?> containingClass;
    private Class<?> type;
    private List<Annotation> annotations;

    public static MethodParameter from(String name, Parameter parameter, Method method) {
        if (Objects.isNull(method) ||
            StringUtils.isEmpty(name) ||
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
