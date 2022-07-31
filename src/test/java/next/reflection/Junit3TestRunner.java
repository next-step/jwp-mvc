package next.reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @DisplayName("test로 시작하는 메소드만 Java Replection을 활용해 실행")
    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Object instance = clazz.getConstructors()[0].newInstance();

        final Method[] methods = clazz.getMethods();

        for (final Method method : methods) {
            if (method.getName().startsWith("test")) {
                method.invoke(instance);
            }
        }

    }
}
