package core.mvc.scanner;

import core.annotation.WebApplication;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class WebApplicationScanner {

    private static final Class<WebApplication> WEB_APPLICATION_ANNOTATION = WebApplication.class;

    private Object[] basePackages = {};
    private Reflections reflections;

    public WebApplicationScanner(Object... basePackages) {
        this.basePackages = basePackages;
        this.reflections = new Reflections(basePackages);
    }

    public void init() {
        Reflections reflections = new Reflections("");
        this.basePackages = reflections.getTypesAnnotatedWith(WEB_APPLICATION_ANNOTATION)
                .stream()
                .map(this::findBasePackages)
                .flatMap(Arrays::stream)
                .toArray();

        if (this.basePackages.length == 0) {
            throw new IllegalStateException("Base packages not initialized");
        }

        this.reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> scanClassesBy(Class<? extends Annotation> annotation) {
        return this.reflections.getTypesAnnotatedWith(annotation, true);
    }

    public <T> Set<Class<? extends T>> scanClassesAssignedBy(Class<T> assignedClass) {
        return this.reflections.getSubTypesOf(assignedClass);
    }

    public List<Method> scanMethodsBy(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    private String[] findBasePackages(Class<?> clazz) {
        String[] packages = clazz.getAnnotation(WEB_APPLICATION_ANNOTATION).basePackages();

        return packages.length == 0 ? new String[]{clazz.getPackage().getName()} : packages;
    }
}
