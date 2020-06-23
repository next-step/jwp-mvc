package core.mvc.support;

import core.annotation.web.RequestParam;

import java.lang.annotation.Annotation;

public class MethodParameter {
    private String name;
    private Class<?> type;
    private Annotation annotation;

    public MethodParameter(String name, Class<?> type, Annotation annotation) {
        this.name = name;
        this.type = type;
        this.annotation = annotation;
    }

    public boolean isSameClass(Class<?> clazz) {
        return type.equals(clazz);
    }

    public String getName() {
        if (annotation.annotationType().equals(RequestParam.class)) {
            String annotationValue = ((RequestParam) annotation).value();
            if (!"".equals(annotationValue)) {
                return annotationValue;
            }
        }
        return name;
    }

}
