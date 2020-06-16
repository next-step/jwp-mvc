package core.mvc.param.parser.annotation;

import core.mvc.param.parser.ValueExtractor;

import java.lang.annotation.Annotation;

public abstract class AnnotationValueExtractor<T extends Annotation> implements ValueExtractor {
    protected Class<T> annotation;

    protected AnnotationValueExtractor(Class<T> annotation) {
        this.annotation = annotation;
    }

    public Class<T> getAnnotation() {
        return annotation;
    }
}
