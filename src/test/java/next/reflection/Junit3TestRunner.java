package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final List<Method> targetMethods = filterMethodsStartsWith(clazz, "test");
        final Junit3Test junit3Test = clazz.newInstance();

        for(Method method : targetMethods) {
            method.invoke(junit3Test);
        }
    }

    private List<Method> filterMethodsStartsWith(Class<?> clazz, String startsWith) {
        final Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(m -> m.getName().startsWith(startsWith))
                .collect(Collectors.toList());
    }
}
