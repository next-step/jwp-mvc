package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        Class<MyTest> annotation = MyTest.class;
        Stream<Method> methods = Arrays
                .stream(clazz.getMethods())
                .filter( method -> method.isAnnotationPresent(annotation));

        methods.forEach(method -> {
            try {
                method.invoke(clazz.newInstance());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }
}
