package core.mvc.tobe;

import java.lang.annotation.Annotation;

public class MethodParameter {

    private Class<?> type;
    private Annotation[] annotations;

    public MethodParameter(Class<?> parameterType, Annotation[] parameterAnnotation) {
        this.type = parameterType;
        this.annotations = parameterAnnotation;
    }

    public Class<?> getType() {
        return type;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public boolean isString() {
        return type == String.class;
    }

    public boolean isInteger() {
        return type == Integer.class || type == int.class;
    }
}
