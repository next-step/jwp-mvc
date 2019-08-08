package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Junit3TestRunner {

    public static final String TEST_PREFIX = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
        for (Method method : methods) {
            if(decideTestMethod(method)) {
                method.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }

    public boolean decideTestMethod(Method method) {
        return method.getName().startsWith(TEST_PREFIX);
    }


}
