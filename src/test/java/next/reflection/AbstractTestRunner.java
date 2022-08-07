package next.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTestRunner {

    private static final int PARAM_COUNT_DEFAULT_CONSTRUCTOR = 0;

    protected void run(Class<?> testClass) throws Exception {
        Constructor<?> defaultConstruct = findDefaultConstruct(testClass);

        List<Method> testMethods = findTestMethods(testClass);

        execute(defaultConstruct, testMethods);
    }

    private Constructor<?> findDefaultConstruct(Class<?> clazz) {
        return Arrays.stream(clazz.getConstructors())
                .filter(c -> c.getParameterCount() == PARAM_COUNT_DEFAULT_CONSTRUCTOR)
                .findAny()
                .orElseThrow();
    }

    private List<Method> findTestMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::isTestMethod)
                .collect(Collectors.toList());
    }

    private void execute(Constructor<?> constructor, List<Method> testMethods) throws Exception{
        for (Method testMethod : testMethods) {
            Object instance = constructor.newInstance();

            testMethod.invoke(instance);
        }
    }

    abstract boolean isTestMethod(Method method);
}
