package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class MethodParameter {

    private final TypeDescriptor typeDescriptor;
    private final Parameter parameter;
    private final Annotation[] annotations;
    private final String name;
    private final int index;

    public MethodParameter(final Parameter parameter, final String name, final int index) {
        this.typeDescriptor = TypeDescriptor.valueOf(parameter.getType());
        this.parameter = parameter;
        this.annotations = parameter.getAnnotations();
        this.name = name;
        this.index = index;
    }

    public boolean equalsType(final Class<?> type) {
        return typeDescriptor.equals(type);
    }

    public Class<?> getType() {
        return typeDescriptor.getType();
    }

    public Object convertStringTo(final String value) {
        return typeDescriptor.convertStringTo(value);
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return parameter.isAnnotationPresent(annotationClass);
    }
}
