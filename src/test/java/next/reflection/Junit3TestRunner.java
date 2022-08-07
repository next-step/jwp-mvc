package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit3TestRunner {

    private static final String PREFIX_TEST_METHOD = "test";

    private static final int PARAM_COUNT_DEFAULT_CONSTRUCTOR = 0;

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Constructor<?> defaultConstruct = findDefaultConstruct(clazz);

        List<Method> testMethods = findTestMethods(clazz);

        execute(defaultConstruct, testMethods);
    }

    private Constructor<?> findDefaultConstruct(Class<Junit3Test> clazz) {
        return Arrays.stream(clazz.getConstructors())
                .filter(c -> c.getParameterCount() == PARAM_COUNT_DEFAULT_CONSTRUCTOR)
                .findAny()
                .orElseThrow();
    }

    private List<Method> findTestMethods(Class<Junit3Test> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith(PREFIX_TEST_METHOD))
                .collect(Collectors.toList());
    }

    private void execute(Constructor<?> constructor, List<Method> testMethods) throws Exception{
        for (Method testMethod : testMethods) {
            Object instance = constructor.newInstance();

            testMethod.invoke(instance);
        }
    }
}
