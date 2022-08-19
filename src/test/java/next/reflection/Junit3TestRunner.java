package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());
        Junit3Test junit3Test = clazz.getConstructor().newInstance();

        for (Method method : methods) {
            method.invoke(junit3Test);
        }
    }
}
