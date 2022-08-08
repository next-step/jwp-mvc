package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {
    private static final String RUN_TEST_PREFIX = "test";

    @DisplayName("test로 시작하는 메소드 실행")
    @Test
    public void run() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Constructor<Junit3Test> constructor = clazz.getConstructor();
        final Junit3Test junit3Test = constructor.newInstance();

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::isRunnableTest)
                .forEach(method -> runTest(junit3Test, method));
    }

    private boolean isRunnableTest(Method method) {
        return method.getName().startsWith(RUN_TEST_PREFIX);
    }

    private void runTest(Junit3Test junit3Test, Method method) {
        try {
            method.invoke(junit3Test);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
