package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {

    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Constructor<Junit4Test> constructor = clazz.getConstructor();
        final Junit4Test instance = constructor.newInstance();

        Method[] methods = clazz.getDeclaredMethods();

        Arrays.stream(methods)
                .filter(m -> m.isAnnotationPresent(MyTest.class))
                .forEach(m -> {
                    try {
                        m.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

    }
}
