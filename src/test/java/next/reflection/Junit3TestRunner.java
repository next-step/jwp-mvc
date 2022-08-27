package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        for (Method method : clazz.getDeclaredMethods()) {
            runtMethod(clazz, method);
        }
    }

    private void runtMethod(Class<Junit3Test> clazz, Method method) throws Exception {
        if (method.getName().startsWith("test")) {
            method.invoke(clazz.newInstance());
        }
    }
}
