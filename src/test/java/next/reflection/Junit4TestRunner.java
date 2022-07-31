package next.reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Object instance = clazz.getConstructors()[0].newInstance();

        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(instance);
            }
        }

    }
}
