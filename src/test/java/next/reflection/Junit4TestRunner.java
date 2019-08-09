package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit4TestRunner {

    @DisplayName("요구사항 3 - @Test 애노테이션 메소드 실행")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Object object = clazz.getConstructor().newInstance();

        for (final Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(MyTest.class)) {
                continue;
            }

            method.invoke(object);
        }
    }
}
