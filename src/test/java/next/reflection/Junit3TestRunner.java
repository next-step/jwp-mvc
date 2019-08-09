package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner {

    @DisplayName("요구사항 2 - test로 시작하는 메소드 실행")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Object object = clazz.getConstructor().newInstance();

        for (final Method method : clazz.getMethods()) {
            if (!method.getName().startsWith("test")) {
                continue;
            }

            method.invoke(object);
        }
    }
}
