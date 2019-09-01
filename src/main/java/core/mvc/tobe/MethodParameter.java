package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author : yusik
 * @date : 2019-08-22
 */
public class MethodParameter {
    private Annotation[] annotations;
    private Class<?> parameterType;
    private String parameterName;
    private String[] urls;

    public MethodParameter(Annotation[] annotations, Class<?> parameterType, String parameterName, String[] urls) {
        this.annotations = annotations;
        this.parameterType = parameterType;
        this.parameterName = parameterName;
        this.urls = urls;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public String[] getUrls() {
        return urls;
    }

    public boolean hasAnnotation(Class<? extends Annotation> target) {
        return Arrays.stream(annotations)
                .anyMatch(annotation -> target.equals(annotation.annotationType()));
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }
}
