package next.reflection;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class Annotations {

    private final Annotation[] annotations;

    public Annotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public boolean hasAnnotation(final Class<?> targetAnnotation) {
        String annotationName = targetAnnotation.getName();
        return Arrays.stream(annotations)
                .filter(a -> annotationName.equals(a.annotationType().getName()))
                .findFirst().isPresent();
    }
}
