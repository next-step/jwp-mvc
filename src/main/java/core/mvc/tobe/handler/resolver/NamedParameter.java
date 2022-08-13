package core.mvc.tobe.handler.resolver;

import java.lang.reflect.Parameter;

public class NamedParameter {
    private final Parameter parameter;
    private final String name;

    public NamedParameter(Parameter parameter, String name) {
        this.parameter = parameter;
        this.name = name;
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public boolean isEqualsType(Class<?> type) {
        return getType().isAssignableFrom(type);
    }

    public String getName() {
        return name;
    }
}
