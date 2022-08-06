package core.mvc.tobe;

import java.lang.reflect.Method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

class ArgumentResolverTest {

    protected final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    protected MethodParameter[] getMethodParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        MethodParameter[] methodParameters = new MethodParameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            methodParameters[i] = new MethodParameter(parameterTypes[i], method, parameterNames[i]);
        }
        return methodParameters;
    }
}
