package core.mvc;

import core.annotation.web.PathVariable;

import java.lang.reflect.Parameter;

public class MethodParameter {
    private String name;
    private Parameter parameter;

    public MethodParameter(String name, Parameter parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public boolean isPathVariable() {
        return parameter.isAnnotationPresent(PathVariable.class);
    }

    public boolean isAnnotationNotExist() {
        return parameter.getAnnotations().length == 0;
    }
}
