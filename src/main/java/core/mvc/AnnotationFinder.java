package core.mvc;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created By kjs4395 on 2020-06-19
 *
 */
public class AnnotationFinder {

    private final Reflections reflections;

    public AnnotationFinder(Object[]... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> findAnnotationClass(Class<? extends Annotation> annotation) {
        return this.reflections.getTypesAnnotatedWith(annotation);
    }
}
