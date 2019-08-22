package core.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ParameterTypeName {
    private final String name;
    private final Parameter parameter;
    private final int index;
    private final Annotation[] annotations;

    public ParameterTypeName(String name, Parameter parameter, int index) {
        this.name = name;
        this.parameter = parameter;
        this.index = index;
        this.annotations = this.parameter.getDeclaredAnnotations();
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return this.parameter.getType();
    }

    public boolean hasAnnotation(Class annotationClass) {
        return Arrays.stream(this.annotations)
                .filter(annotation -> annotation.equals(annotationClass))
                .findFirst()
                .isPresent();
    }

    public Annotation[] getAnnotationsByType(Class annotationClass) {
        return this.parameter.getAnnotationsByType(annotationClass);
    }

    @Override
    public String toString() {
        return "ParameterTypeName{" +
                "name='" + name + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}
