package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.TestUserController;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public abstract class AbstractMethodArgumentResolverTest {

    private static final ParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    Method getMethodOfTestUserController(final String methodName) {
        final Class<?> clazz = TestUserController.class;
        return getMethod(methodName, clazz.getDeclaredMethods());
    }

    Method getMethod(final String name, final Method[] methods) {
        return Arrays.stream(methods)
            .filter(method -> method.getName().equals(name))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    String[] getParameterNames(final Method method) {
        final String[] parameterNames = DISCOVERER.getParameterNames(method);
        if (parameterNames == null) {
            Assertions.fail();
        }
        return parameterNames;
    }
}
