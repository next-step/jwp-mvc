package next.reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Object instance = clazz.getConstructors()[0].newInstance();

        final Method[] methods = clazz.getMethods();

        for (final Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }

    }
}
