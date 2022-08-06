package core.mvc.tobe;

import java.lang.reflect.Method;

class ArgumentResolverTest {

    protected MethodParameter[] getMethodParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        MethodParameter[] methodParameters = new MethodParameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            methodParameters[i] = new MethodParameter(parameterTypes[i], method);
        }
        return methodParameters;
    }
}
