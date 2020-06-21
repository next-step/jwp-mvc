package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Junit3Test junit3Test = new Junit3Test();

        Class<Junit3Test> clazz = Junit3Test.class;
        Stream.of(clazz.getDeclaredMethods())
            .filter(method -> method.getName().startsWith("test"))
            .forEach(method -> {
                try {
                    method.invoke(junit3Test);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
    }
}
