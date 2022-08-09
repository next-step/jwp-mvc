package next.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        var instance = clazz.newInstance();

        var methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(it -> it.getName().startsWith("test"))
            .collect(Collectors.toList());

        for (Method method : methods) {
            method.invoke(instance);
        }
    }
}
