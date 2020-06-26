package core.mvc.support;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.annotation.Annotation;
import java.util.*;

public class MethodParameter {
    private final String name;
    private final Class<?> type;
    private final List<Annotation> annotations;
    private final PathPattern pathPattern;

    public MethodParameter(String name, Class<?> type, List<Annotation> annotation, PathPattern pathPattern) {
        this.name = name;
        this.type = type;
        this.annotations = Collections.unmodifiableList(annotation);
        this.pathPattern = pathPattern;
    }

    public boolean matchClass(Class<?> clazz) {
        return type.equals(clazz);
    }

    public Class<?> getType() {
        return type;
    }

    public Optional<String> getPathVariable(String path, String name) {
        final Map<String, String> variables = pathPattern.matchAndExtract(PathContainer.parsePath(path)).getUriVariables();
        return Optional.ofNullable(variables.get(name));
    }

    public boolean anyMatchClass(Class<?>... classes) {
        return Arrays.stream(classes)
                .anyMatch(this::matchClass);
    }

    public String getName() {
        return name;
    }

    public Annotation getAnnotation(Class<?> clazz) {
        return annotations.stream()
                .filter(a -> a.annotationType().equals(clazz))
                .findFirst()
                .orElse(null);
    }

    public boolean isEmptyAnnotation() {
        return annotations.isEmpty();
    }

    public boolean hasAnnotationType(Class<?> clazz) {
        return annotations.stream()
                .anyMatch(a -> a.annotationType().equals(clazz));
    }

}
