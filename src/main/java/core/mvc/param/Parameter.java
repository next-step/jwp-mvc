package core.mvc.param;

import core.mvc.param.extractor.ValueExtractors;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

public class Parameter {
    private final Class<?> type;
    private final String name;
    private final Class<? extends Annotation> annotation;

    public Parameter(String name, Class<?> type, Class<? extends Annotation> annotation) {
        this.name = name;
        this.type = type;
        this.annotation = annotation;
    }

    public Class<?> getTypeClass() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public Object extractValue(HttpServletRequest request) {
        return ValueExtractors.extractValue(this, request);
    }

    public boolean isParamExist(HttpServletRequest request) {
        System.out.println("Type : " + type + " / name : " + name + " / value : " + extractValue(request) );
        return extractValue(request) != null;
    }
}
