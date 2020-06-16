package core.mvc.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class AnnotationScanner {

    public static Set<Class<?>> loadClasses(Class<? extends Annotation> annotation, Object... basePackages) {
        return null;
    }

    public static Set<Method> loadMethods(Class<?> targetClass, Class<? extends Annotation> annotation) {
        return null;
    }
}
