package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {

    private static final String PREFIX_METHOD = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if(method.getName().startsWith(PREFIX_METHOD)){
                method.invoke(clazz.newInstance());
            }
        }
    }
}
