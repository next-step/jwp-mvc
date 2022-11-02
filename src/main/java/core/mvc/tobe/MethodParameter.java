package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class MethodParameter {

    private final Method method;
    private final String parameterName;
    private final Parameter parameter;

    public Method getMethod() {
        return method;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Class<?> getParameterType() {
        return parameter.getType();
    }

    public Annotation[] getAnnotations() {
        return parameter.getAnnotations();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public MethodParameter(Method method, String parameterName, Parameter parameter) {
        this.method = method;
        this.parameterName = parameterName;
        this.parameter = parameter;
    }

    public boolean hasAnnotationInParameter(Class<?> clazz) {
        Annotation[] annotations = getAnnotations();
        return Arrays.stream(annotations)
                .anyMatch(annotation -> annotation.annotationType() == clazz);
    }

    public String getPathVariableValue() {
        return parameter.getAnnotation(PathVariable.class).value();
    }

    public String getRequestMappingValue() {
        return method.getAnnotation(RequestMapping.class).value();
    }
}
