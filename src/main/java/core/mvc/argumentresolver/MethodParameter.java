package core.mvc.argumentresolver;

import lombok.Builder;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

@Getter
public class MethodParameter {

    private Parameter parameter;
    private String parameterName;
    private String requestMappingUri;

    @Builder
    public MethodParameter(Parameter parameter, String parameterName, String requestMappingUri) {
        this.parameter = parameter;
        this.parameterName = parameterName;
        this.requestMappingUri = requestMappingUri;
    }

    public boolean isAssignableFrom(Class<?> clazz) {
        return parameter.getType().isAssignableFrom(clazz);
    }

    public boolean matchParameterName(String parameterName) {
        return this.parameterName.equals(parameterName);
    }

    public Class<?> getParameterType() {
        return this.parameter.getType();
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        return parameter.isAnnotationPresent(annotation);
    }
}
