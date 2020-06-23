package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class Junit4TestRunner {

    @Test
    public void run() throws Exception {
        Junit4Test junit4Test = new Junit4Test();

        Class<Junit4Test> clazz = Junit4Test.class;

        Stream.of(clazz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .forEach(method -> {
                try {
                    method.invoke(junit4Test);
                } catch (IllegalAccessException| InvocationTargetException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            });
    }
}
