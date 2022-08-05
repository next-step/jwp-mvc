package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {
    @Test
    void run() {
        Class<Junit4Test> clazz = Junit4Test.class;

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> {
                    try {
                        method.invoke(clazz.getConstructor().newInstance());
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                             InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
