package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            invokeIfMatches(method, clazz);
        }
    }

    private void invokeIfMatches(Method method, Class<Junit4Test> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (method.isAnnotationPresent(MyTest.class)) {
            method.invoke(clazz.newInstance());
        }
    }
}
