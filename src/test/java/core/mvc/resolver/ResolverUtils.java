package core.mvc.resolver;

import core.mvc.tobe.TestUserController;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResolverUtils {

    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public static Method getMethod(String methodName) throws NoSuchMethodException {
        Class<TestUserController> clazz = TestUserController.class;
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .findFirst()
                .orElseThrow(NoSuchMethodException::new);
    }

    public static List<MethodParameter> generateMethodParameters(Method method) {
        List<MethodParameter> methodParameters = new ArrayList<>();

        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        for (int i = 0; i < parameters.length; i++) {
            MethodParameter methodParameter = new MethodParameter(method, parameters[i], parameterNames[i]);
            methodParameters.add(methodParameter);
        }

        return methodParameters;
    }
}
