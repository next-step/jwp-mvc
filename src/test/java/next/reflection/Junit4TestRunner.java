package next.reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @DisplayName("@MyTest 애노테이션으로 설정되어 있는 메소드만 Java Replection을 활용해 실행")
    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Object instance = clazz.getConstructors()[0].newInstance();

        final Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(MyTest.class)) {
                method.invoke(instance);
            }
        }

    }
}
