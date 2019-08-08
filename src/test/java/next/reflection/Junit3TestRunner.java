package next.reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {

    private static final String TEST = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for(Method method : declaredMethods) {
            invokeMethod(clazz, method);
        }
    }

    private void invokeMethod(Class<Junit3Test> clazz, Method method) throws Exception {
        if (isMethodNameStartWithTest(method)) {
            method.invoke(clazz.newInstance());
        }
    }

    private boolean isMethodNameStartWithTest(Method method) {
        return method.getName().startsWith(TEST);
    }
}
