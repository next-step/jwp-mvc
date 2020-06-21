package core.mvc.support;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Objects;

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

    public @Nullable <A extends Annotation> A getParameterAnnotation(Class<A> annotation) {
        Objects.requireNonNull(annotation, "Given annotation is null.");
        for (Annotation parameterAnnotation : getParameterAnnotations()) {
            if (annotation.isInstance(parameterAnnotation)) {
                return (A) parameterAnnotation;
            }
        }
        return null;
    }

}
