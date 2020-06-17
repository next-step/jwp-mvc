package core.mvc.param;

import core.mvc.param.extractor.ValueExtractors;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Objects;

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
        return extractValue(request) != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(type, parameter.type) &&
                Objects.equals(name, parameter.name) &&
                Objects.equals(annotation, parameter.annotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, annotation);
    }
}
