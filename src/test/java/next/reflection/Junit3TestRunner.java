package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().contains("test"))
                .forEach(method -> {
                    try {
                        method.invoke(clazz.newInstance());
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException ignored) {}
                });
    }
}
