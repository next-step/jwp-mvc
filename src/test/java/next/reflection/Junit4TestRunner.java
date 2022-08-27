package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        for (Method method : clazz.getDeclaredMethods()) {
            runMethodByMyTestAnnotation(clazz, method);
        }
    }

    private void runMethodByMyTestAnnotation(Class<Junit4Test> clazz, Method declaredMethod)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (declaredMethod.isAnnotationPresent(MyTest.class)) {
            declaredMethod.invoke(clazz.newInstance());
        }
    }
}
