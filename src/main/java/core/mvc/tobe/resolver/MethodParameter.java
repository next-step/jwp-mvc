package core.mvc.tobe.resolver;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

public class MethodParameter {
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    private static final Set<Class<?>> wrapperClasses = Set.of(Boolean.class, Integer.class, Long.class, Double.class, String.class);

    private final Method method;
    private final Parameter parameter;
    private final int parameterIndex;

    public MethodParameter(Method method, Parameter parameter, int parameterIndex) {
        this.method = method;
        this.parameter = parameter;
        this.parameterIndex = parameterIndex;
    }

    public boolean hasParameterAnnotation(Class<? extends Annotation> annotation) {
        return parameter.isAnnotationPresent(annotation);
    }

    public boolean isParameterTypeEqual(Class<?> classType) {
        return parameter.getType() == (classType);
    }

    public String getParameterName() {
        String[] parameterNames = NAME_DISCOVERER.getParameterNames(method);
        return parameterNames[parameterIndex];
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public <T extends Annotation> T getMethodAnnotation(Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    public <T extends Annotation> T getParameterAnnotation(Class<T> annotation) {
        return parameter.getAnnotation(annotation);
    }

    public boolean isPrimitiveOrWrapperType() {
        return parameter.getType().isPrimitive() || wrapperClasses.contains(parameter.getType());
    }

    public boolean isModelType() {
        return !isPrimitiveOrWrapperType()
                && HttpServletRequest.class != parameter.getType()
                && HttpServletResponse.class != parameter.getType();
    }
}
