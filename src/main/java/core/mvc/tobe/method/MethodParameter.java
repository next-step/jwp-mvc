package core.mvc.tobe.method;

import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class MethodParameter {

    private final Method method;
    private final Parameter parameter;
    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private MethodParameter(Method method, Parameter parameter) {
        Assert.notNull(method, "method는 null이 될 수 없습니다.");
        Assert.notNull(parameter, "parameter는 null이 될 수 없습니다.");
        this.method = method;
        this.parameter = parameter;
    }

    public static MethodParameter of(Method method, Parameter parameter) {
        return new MethodParameter(method, parameter);
    }

    public boolean hasMethodAnnotation(Class<RequestMapping> requestMappingClass) {
        return method.isAnnotationPresent(requestMappingClass);
    }

    public boolean hasParameterAnnotation(Class<? extends Annotation> annotation) {
        return parameter.isAnnotationPresent(annotation);
    }

    public <T extends Annotation> T methodAnnotation(Class<T> annotation) {
        return method.getDeclaredAnnotation(annotation);
    }

    public <T extends Annotation> T parameterAnnotation(Class<T> annotation) {
        return parameter.getDeclaredAnnotation(annotation);
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public String getName() {
        return nameDiscoverer.getParameterNames(method)[paramIndex()];
    }

    private int paramIndex() {
        return List.of(method.getParameters())
                .indexOf(parameter);
    }

    public boolean annotationNotExist() {
        return parameter.getDeclaredAnnotations().length == 0;
    }
}
