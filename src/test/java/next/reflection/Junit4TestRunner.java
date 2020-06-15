package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Junit4TestRunner {

    private static final Logger log = LoggerFactory.getLogger(Junit4TestRunner.class);

    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Method[] methods = getMethods(clazz);
        final Constructor<Junit4Test> constructor = determineNonArgsConstructor(clazz);
        final Junit4Test instance = constructor.newInstance();

        for (Method method : methods) {
            if (isAnnotatedWithMyTest(method)) {
                method.invoke(instance);
            }
        }
    }

    private Method[] getMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

    private boolean isAnnotatedWithMyTest(Method method) {
        return method.isAnnotationPresent(MyTest.class);
    }

    private Constructor<Junit4Test> determineNonArgsConstructor(Class<Junit4Test> clazz) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        return null;
    }
}
