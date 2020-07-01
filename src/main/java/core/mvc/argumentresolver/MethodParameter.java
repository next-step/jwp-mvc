package core.mvc.argumentresolver;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Parameter;

@Getter
public class MethodParameter {

    private Parameter parameter;
    private String parameterName;

    @Builder
    public MethodParameter(Parameter parameter, String parameterName) {
        this.parameter = parameter;
        this.parameterName = parameterName;
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
}
