package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Junit3TestRunner {

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Method[] methods = getMethods(clazz);
        for (Method method : methods) {
            if (startsWithTest(method.getName())) {
                final Constructor<Junit3Test> constructor = determineNonArgsConstructor(clazz);
                final Junit3Test test = constructor.newInstance();
                method.invoke(test);
            }
        }
    }

    private Method[] getMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

    private boolean startsWithTest(String value) {
        return value != null && value.startsWith("test");
    }

    private Constructor<Junit3Test> determineNonArgsConstructor(Class<Junit3Test> clazz) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        return null;
    }
}
