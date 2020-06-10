package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        invokeMethods(
            clazz.newInstance(),
            clazz.getDeclaredMethods()
        );
    }

    private void invokeMethods(Junit4Test junit4Test, Method[] methods) {
        if (methods.length <= 0) {
            return;
        }

        Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .forEach(method -> {
                try {
                    method.invoke(junit4Test);
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
    }
}
