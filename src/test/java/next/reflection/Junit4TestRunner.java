package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        Junit4Test newInstance = clazz.getDeclaredConstructor().newInstance();

        for (Method method : methods) {
            runMethod(newInstance, method);
        }
    }

    private void runMethod(Junit4Test clazz, Method method) throws InvocationTargetException, IllegalAccessException {
        if (method.isAnnotationPresent(MyTest.class)) {
            method.invoke(clazz);
        }
    }
}
