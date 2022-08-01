package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String methodName = method.getName();

            if(methodName.startsWith("test")) {
                method.invoke(clazz.newInstance());
            }
        }
    }
}
