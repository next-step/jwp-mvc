package core.mvc.tobe;

import core.annotation.web.Controller;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

class ControllerAnnotationScanner implements AnnotationScanner {

    private final Set<Class<?>> controllers;

    ControllerAnnotationScanner(final Reflections reflections) {
        Objects.requireNonNull(reflections, "reflections 객체는 필수입니다.");

        controllers = new HashSet<>(reflections.getTypesAnnotatedWith(Controller.class));
    }

    Stream<Method> filteredMethodsBy(final Class<? extends Annotation> annotationClass) {
        return controllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(annotationClass));
    }
}
