package core.mvc.support;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class MethodSignature {

    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private final Method method;
    private final List<MethodParameter> methodParameters;

    public MethodSignature(Method method) {
        this.method = method;
        this.methodParameters = new ArrayList<>();

        final String[] parameterNames = nameDiscoverer.getParameterNames(method);
        final Parameter[] parameters = method.getParameters();
        for (int paramIdx = 0; paramIdx < parameters.length; paramIdx++) {
            final Parameter parameter = parameters[paramIdx];
            final Annotation[] annotations = parameter.getAnnotations();
            final MethodParameter methodParameter =
                    new MethodParameter(method, paramIdx, parameter, parameterNames[paramIdx], annotations);
            methodParameters.add(methodParameter);
        }
    }

    public List<MethodParameter> getMethodParameters() {
        return methodParameters;
    }
}
