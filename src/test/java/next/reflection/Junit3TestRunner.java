package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {

    @DisplayName("이름이 test로 시작하는 메소드만 실행")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        Junit3Test junit3TestInstance = clazz.newInstance();

        Arrays.stream(declaredMethods)
                .filter(method -> method.getName().startsWith("test"))
                .forEach(testMethod -> {
                    try {
                        testMethod.invoke(junit3TestInstance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
