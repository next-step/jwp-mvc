package core.mvc.scanner;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public final class AnnotationScanner {

    private static final Logger log = LoggerFactory.getLogger(AnnotationScanner.class);

    public static Set<Class<?>> loadClasses(Class<? extends Annotation> annotation, Object... basePackages) {
        final Reflections reflections = new Reflections(
                ConfigurationBuilder
                        .build(basePackages)
                        .setScanners(
                                new SubTypesScanner(false),
                                new TypeAnnotationsScanner()
                        )
        );
        return reflections.getTypesAnnotatedWith(annotation);
    }

    public static Set<Method> loadMethods(Class<?> targetClass, Class<? extends Annotation> annotation) {
        return Arrays.stream(targetClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }
}
