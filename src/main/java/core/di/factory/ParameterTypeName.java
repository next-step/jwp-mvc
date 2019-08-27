package core.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ParameterTypeName {
    private final String name;
    private final Parameter parameter;
    private final Annotation[] annotations;

    public ParameterTypeName(String name, Parameter parameter) {
        this.name = name;
        this.parameter = parameter;
        this.annotations = this.parameter.getDeclaredAnnotations();
    }
    
    public ParameterTypeName(ParameterTypeName parameterTypeName) {
        this.name = parameterTypeName.getName();
        this.parameter = parameterTypeName.getParameter();
        this.annotations = parameterTypeName.getAnnotations();
    }
    

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return this.parameter.getType();
    }

    public Parameter getParameter() {
		return parameter;
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        return this.parameter.isAnnotationPresent(annotationClass);
    }

    @Override
    public String toString() {
        return "ParameterTypeName{" +
                "name='" + name + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}
