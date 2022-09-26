package core.mvc.tobe.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ArgumentModel {
    private final Method method;
    private final Class<?> type;
    private final Annotation[] annotations;
    private final String parameterName;

    public ArgumentModel(Method method, Class<?> type, Annotation[] annotations, String parameterName) {
        this.method = method;
        this.type = type;
        this.annotations = annotations;
        this.parameterName = parameterName;
    }

    public Method method() {
        return method;
    }

    public Class<?> type() {
        return type;
    }

    public Annotation[] annotations() {
        return annotations;
    }

    public String parameterName() {
        return parameterName;
    }
}
