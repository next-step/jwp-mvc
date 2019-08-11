package next.reflection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static next.reflection.ReflectionUtils.invokeMethod;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> invokeMethod(clazz, method));
    }
}
