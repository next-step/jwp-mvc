package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit3TestRunner {

    private static final String TEST_METHOD_NAME_PREFIX = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        List<Method> testMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith(TEST_METHOD_NAME_PREFIX))
                .collect(Collectors.toList());

        Junit3Test instance = clazz.newInstance();
        for (Method method : testMethods) {
            method.invoke(instance);
        }
    }
}
