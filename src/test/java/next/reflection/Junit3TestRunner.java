package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {
    @Test
    public void run() {
        Class<Junit3Test> clazz = Junit3Test.class;

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::isTestMethod)
                .forEach(it -> this.invokeMethod(clazz, it));
    }

    private boolean isTestMethod(Method method) {
        return method.getName().startsWith("test");
    }

    private Object invokeMethod(Class<Junit3Test> clazz, Method method) {
        try {
            return method.invoke(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
