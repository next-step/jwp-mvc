package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            invokeWithAnnotation(clazz, method, MyTest.class);
        }
    }

    private void invokeWithAnnotation(Class clazz, Method method, Class annotation) {
        if (method.isAnnotationPresent(annotation)) {
            invoke(clazz, method);
        }
    }

    private void invoke(Class clazz, Method method) {
        try {
            method.invoke(clazz.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            ex.printStackTrace();
        }
    }
}
