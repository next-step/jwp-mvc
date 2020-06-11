package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {

    private final String INVOCABLE_METHODS_PREFIX = "test";

    @Test
    public void run() throws IllegalAccessException, InstantiationException {
        Class<Junit3Test> clazz = Junit3Test.class;

        invokeMethods(
            clazz.newInstance(),
            clazz.getDeclaredMethods()
        );
    }

    private void invokeMethods(Junit3Test junit3Test, Method[] methods) {
        if (methods.length <= 0) {
            return;
        }

        Arrays.stream(methods)
            .filter(method -> method.getName().startsWith(INVOCABLE_METHODS_PREFIX))
            .forEach(method -> {
                try {
                    method.invoke(junit3Test);
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
    }
}
