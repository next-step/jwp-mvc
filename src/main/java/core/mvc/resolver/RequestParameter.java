package core.mvc.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestParameter {
    private Method method;
    private Class<?> classType;
    private Annotation[] annotations;
    private String parameterName;

    public RequestParameter(Method method, Parameter methodParameter, String parameterName) {
        this.method = method;
        this.classType = methodParameter.getType();
        this.annotations = methodParameter.getAnnotations();
        this.parameterName = parameterName;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public Method getMethod() {
        return method;
    }

    public String getParameterName() {
        return parameterName;
    }
}
