package core.mvc.args;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by iltaek on 2020/06/29 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class MethodParameter {

    private final Method method;
    private final String parameterName;
    private final Parameter parameter;

    public MethodParameter(Method method, String parameterName, Parameter parameter) {
        this.method = method;
        this.parameterName = parameterName;
        this.parameter = parameter;
    }

    public boolean isParameterAnnotationPresent(Class<? extends Annotation> annotation) {
        return parameter.isAnnotationPresent(annotation);
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public String getName() {
        return parameterName;
    }

    @Override
    public String toString() {
        return String.format("%s %s", parameter.getType().getName(), parameterName);
    }

    public <T extends Annotation> T getMethodAnnotation(Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }
}
