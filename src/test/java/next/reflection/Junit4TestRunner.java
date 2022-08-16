package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit4TestRunner {

    private static final int PARAM_COUNT_DEFAULT_CONSTRUCTOR = 0;

    @Test
    public void run() throws Exception {
        Constructor<?> defaultConstruct = findDefaultConstruct();

        List<Method> testMethods = findTestMethods();

        execute(defaultConstruct, testMethods);
    }

    private Constructor<?> findDefaultConstruct() {
        return Arrays.stream(Junit4Test.class.getConstructors())
                .filter(c -> c.getParameterCount() == PARAM_COUNT_DEFAULT_CONSTRUCTOR)
                .findAny()
                .orElseThrow();
    }

    private List<Method> findTestMethods() {
        return Arrays.stream(Junit4Test.class.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .collect(Collectors.toList());
    }

    private void execute(Constructor<?> constructor, List<Method> testMethods) throws Exception{
        for (Method testMethod : testMethods) {
            Object instance = constructor.newInstance();

            testMethod.invoke(instance);
        }
    }
}
