package core.mvc.argumentresolver;

import lombok.Getter;

@Getter
public class MethodParameter {

    private Class<?> parameterType;
    private String parameterName;

    public MethodParameter(Class<?> parameterType, String parameterName) {
        this.parameterType = parameterType;
        this.parameterName = parameterName;
    }

    public boolean isAssignableFrom(Class<?> clazz) {
        return parameterType.isAssignableFrom(clazz);
    }
}
