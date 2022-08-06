package core.mvc.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

class ArgumentResolverTest {

    protected final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    protected MethodParameter[] getMethodParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        MethodParameter[] methodParameters = new MethodParameter[parameterTypes.length];
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameterTypes.length; i++) {
            Annotation[] annotations = parameters[i].getAnnotations();
            methodParameters[i] = new MethodParameter(parameterTypes[i], method, parameterNames[i], annotations);
        }
        return methodParameters;
    }
}
