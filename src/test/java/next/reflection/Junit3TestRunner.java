package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3TestRunner {
    private static final String TEST = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            executeTest(clazz, method);
        }

    }

    private void executeTest(Class<Junit3Test> clazz, Method method) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (method.getName().contains(TEST)) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        }
    }
}
