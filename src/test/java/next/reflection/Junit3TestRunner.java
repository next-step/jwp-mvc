package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("test")) {
                m.invoke(new Junit3Test());
            }
        }
    }
}
