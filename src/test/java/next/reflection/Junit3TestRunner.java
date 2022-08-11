package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if (declaredMethod.getName().startsWith("test")) {
                declaredMethod.invoke(clazz.getDeclaredConstructor().newInstance());
            }
        }
    }
}
