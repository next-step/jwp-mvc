package next.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        var instance = clazz.newInstance();

        var methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(it -> it.isAnnotationPresent(MyTest.class))
            .collect(Collectors.toList());

        for (Method method : methods) {
            method.invoke(instance);
        }
    }
}
