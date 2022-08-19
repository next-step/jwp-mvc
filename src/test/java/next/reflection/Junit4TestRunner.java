package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .collect(Collectors.toList());
        Junit4Test junit4Test = clazz.getConstructor().newInstance();

        for (Method method : methods) {
            method.invoke(junit4Test);
        }
    }
}
