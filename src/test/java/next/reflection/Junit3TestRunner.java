package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            invokeStartsWithTest(clazz, method);
        }
    }

    private void invokeStartsWithTest(Class clazz, Method method) {
        if (method.getName().startsWith("test")) {
            invoke(method, clazz);
        }
    }

    private void invoke(Method method, Class clazz) {
        try {
            method.invoke(clazz.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException exception) {
            exception.printStackTrace();
        }
    }
}
