package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test instance = clazz.newInstance();
        for (Method method : clazz.getDeclaredMethods()) {
            if(method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }
    }
}
