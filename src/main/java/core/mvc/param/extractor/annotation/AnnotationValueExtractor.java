package core.mvc.param.extractor.annotation;

import core.mvc.param.extractor.ValueExtractor;

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
