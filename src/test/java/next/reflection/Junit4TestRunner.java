package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();

        Arrays.stream(declaredMethods).filter(it -> it.isAnnotationPresent(MyTest.class)).forEach(method ->
            {
                try {
                    method.invoke(clazz.newInstance());
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }

        );
    }
}
