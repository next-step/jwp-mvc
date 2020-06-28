package core.mvc.tobe;

import core.annotation.web.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodParameter {

    private Method method;
    private String parameterName;
    private Class<?> type;
    private Annotation[] annotations;

    public MethodParameter(Method method, String parameterName, Class<?> type, Annotation[] annotations) {
        this.method = method;
        this.parameterName = parameterName;
        this.type = type;
        this.annotations = annotations;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isHttpServletRequest() {
        return type == HttpServletRequest.class;
    }

    public boolean isHttpServletResponse() {
        return type == HttpServletResponse.class;
    }

    public String getParameterName() {
        return parameterName;
    }

    public boolean isRequestParameterType() {
        return type.isPrimitive() || type == String.class;
    }

    public boolean isPrimitiveType() {
        return type.isPrimitive();
    }

    public boolean isPathVariable() {
        return Arrays.stream(annotations)
                .anyMatch(a -> a.annotationType().equals(PathVariable.class));
    }
}
