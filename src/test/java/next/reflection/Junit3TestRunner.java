package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {
    private static final String EXECUTE_METHOD_PREFIX = "test";

    @Test
    void run() {
        Class<Junit3Test> clazz = Junit3Test.class;
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith(EXECUTE_METHOD_PREFIX))
                .forEach(method -> {
                    try {
                        method.invoke(clazz.getConstructor().newInstance());
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
