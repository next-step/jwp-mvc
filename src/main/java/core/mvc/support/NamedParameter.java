package core.mvc.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class NamedParameter {

    private final Parameter parameter;
    private final String parameterName;

    public NamedParameter(Parameter parameter, String parameterName) {
        this.parameter = parameter;
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Class<?> getParameterType() {
        return parameter.getType();
    }

    public Annotation[] getParameterAnnotations() {
        return parameter.getAnnotations();
    }

    public boolean isAnnotated() {
        return getParameterAnnotations().length != 0;
    }
}
