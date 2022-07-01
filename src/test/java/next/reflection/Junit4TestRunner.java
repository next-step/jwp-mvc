package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::isTestMethod)
                .forEach(it -> this.invokeMethod(clazz, it));
    }

    private boolean isTestMethod(Method method) {
        return method.isAnnotationPresent(MyTest.class);
    }

    private Object invokeMethod(Class<Junit4Test> clazz, Method method) {
        try {
            return method.invoke(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
