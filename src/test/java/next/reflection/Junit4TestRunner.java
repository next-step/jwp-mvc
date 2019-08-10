package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static next.reflection.ReflectionUtils.invokeMethod;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> invokeMethod(clazz, method));
    }
}
