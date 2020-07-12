package core.mvc.tobe;

import core.mvc.utils.DataParser;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodParameter {

    private Method method;
    private final String parameterName;
    private final Class<?> parameterType;
    private final Annotation[] annotations;

    public MethodParameter(String parameterName, Class<?> parameterType) {
        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.annotations = new Annotation[]{};
    }

    public MethodParameter(Method method, String parameterName, Class<?> parameterType, Annotation[] annotations) {
        this.method = method;
        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.annotations = annotations;
    }

    public Class<?> getType() {
        return this.parameterType;
    }

    public String getName() {
        return this.parameterName;
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        return Arrays.stream(this.annotations)
                .anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }

    public boolean hasAnnotation() {
        return this.annotations.length != 0;
    }

    public boolean isSimpleDataType() {
        return DataParser.supports(this.parameterType);
    }

    public Method getMethod() {
        return this.method;
    }
}
