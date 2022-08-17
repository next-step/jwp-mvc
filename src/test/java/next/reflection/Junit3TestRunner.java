package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {
    @Test
    public void run() {
        Class<Junit3Test> clazz = Junit3Test.class;

        Stream<Method> methods = Arrays
                .stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"));

        methods.forEach(method -> {
            try {
                method.invoke(clazz.newInstance());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }
}
