package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Constructor<Junit4Test> constructor = clazz.getConstructor();

        List<Method> availMethods = Arrays.stream(clazz.getMethods())
                .filter(m -> m.isAnnotationPresent(MyTest.class))
                .collect(Collectors.toList());

        for (Method method : availMethods) {
            method.invoke(constructor.newInstance());
        }
    }
}
