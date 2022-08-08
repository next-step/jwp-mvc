package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {

    private static final Class<MyTest> RUN_TEST_ANNOTATION = MyTest.class;

    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Constructor<Junit4Test> constructor = clazz.getConstructor();
        final Junit4Test junit4Test = constructor.newInstance();

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::isRunnableTest)
                .forEach(method -> runTest(junit4Test, method));
    }

    private boolean isRunnableTest(Method method) {
        return method.isAnnotationPresent(RUN_TEST_ANNOTATION);
    }

    private void runTest(Junit4Test junit4Test, Method method) {
        try {
            method.invoke(junit4Test);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
