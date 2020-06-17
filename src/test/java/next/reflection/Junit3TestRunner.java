package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().contains("test")) {
                method.invoke(clazz.newInstance());
            }
        }
    }
}
