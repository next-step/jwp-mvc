package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodParameter {

    private final Class<?> type;
    private final Method method;
    private final String parameterName;
    private final Annotation[] annotations;

    public MethodParameter(Class<?> type, Method method, String parameterName, Annotation[] annotations) {
        this.type = type;
        this.method = method;
        this.parameterName = parameterName;
        this.annotations = annotations;
    }

    public <A extends Annotation> boolean hasAnnotation(Class<A> annotationType) {
        return getAnnotation(annotationType) != null;
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return (A) Arrays.stream(annotations)
            .filter(annotationType::isInstance)
            .findFirst()
            .orElse(null);
    }

    public boolean matches(Class<?> type) {
        return this.type == type;
    }

    public Class<?> getType() {
        return type;
    }

    public Method getMethod() {
        return method;
    }

    public String getParameterName() {
        return parameterName;
    }
}
