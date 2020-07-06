package core.mvc.tobe;

import java.lang.reflect.Parameter;

public class MethodParameter {

    private final String parameterName;
    private final Parameter parameter;

    public MethodParameter(String parameterName, Parameter parameter) {
        this.parameterName = parameterName;
        this.parameter = parameter;
    }

    public Class<?> getType() {
        return this.parameter.getType();
    }

    public String getName() {
        return this.parameterName;
    }
}
