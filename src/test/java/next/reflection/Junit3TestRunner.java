package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {

    @Test
    public void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;
        final Constructor<Junit3Test> constructor = clazz.getConstructor();
        final Junit3Test instance = constructor.newInstance();

        Method[] methods = clazz.getDeclaredMethods();

        Arrays.stream(methods)
                .filter(m -> m.getName().startsWith("test"))
                .forEach(m -> {
                    try {
                        m.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

}
