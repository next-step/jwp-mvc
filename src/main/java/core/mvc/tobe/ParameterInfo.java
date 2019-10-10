package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class ParameterInfo {

    private final Parameter parameter;
    private final String parameterName;
    private final String path;

    public ParameterInfo(final Parameter parameter,
                         final String parameterName,
                         final String path) {
        this.parameter = parameter;
        this.parameterName = parameterName;
        this.path = path;
    }

    public <T extends Annotation> Optional<T> getAnnotation(final Class<T> clazz) {
        return Optional.ofNullable(parameter.getAnnotation(clazz));
    }

    public String getParameterName() {
        return parameterName;
    }

    public boolean matchType(final Class<?> clazz) {
        return clazz.isAssignableFrom(getType());
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public String getPath() {
        return path;
    }

    public boolean isPrimitive() {
        return getType().isPrimitive();
    }

    @Override
    public String toString() {
        return "ParameterInfo{" +
                "parameter=" + parameter +
                ", parameterName='" + parameterName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
