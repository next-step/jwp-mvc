package core.mvc.tobe;

public class MethodParameter {

    private final String parameterName;
    private final Class<?> parameterType;

    public MethodParameter(String parameterName, Class<?> parameterType) {
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public Class<?> getType() {
        return this.parameterType;
    }

    public String getName() {
        return this.parameterName;
    }
}
