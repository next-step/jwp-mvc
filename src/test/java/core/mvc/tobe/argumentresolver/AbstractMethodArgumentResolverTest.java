package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.TestUserController;
import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class AbstractMethodArgumentResolverTest {

    Method getMethodOfTestUserController(final String methodName) {
        final Class<?> clazz = TestUserController.class;
        return getMethod(methodName, clazz.getDeclaredMethods());
    }

    Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
            .filter(method -> method.getName().equals(name))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
