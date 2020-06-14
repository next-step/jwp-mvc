package core.mvc.tobe;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotatedTargetScanner {
    private AnnotatedTargetScanner() {}

    public static Set<Class<?>> loadClasses(final Class<? extends Annotation> annotation,
                                            final Object... basePackage) {
        Reflections reflections = new Reflections(
                basePackage,
                new SubTypesScanner(false),
                new TypeAnnotationsScanner()
        );

       return reflections.getTypesAnnotatedWith(annotation);
    }

    public static List<Method> loadMethodsFromClass(final Class<?> clazz,
                                                    final Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }
}
