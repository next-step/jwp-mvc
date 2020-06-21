package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

@DisplayName("Junit3TestRunner 관련 테스트")
public class Junit3TestRunner {

    @DisplayName("Junit3Test에서 test로 시작하는 메소드를 자동으로 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Constructor<?> constructor = clazz.getConstructor();

        Method[] declaredMethods = clazz.getDeclaredMethods();

        Arrays.stream(declaredMethods)
                .filter(declaredMethod -> {
                    String methodName = declaredMethod.getName();
                    return methodName.startsWith("test");
                })
                .forEach(methodStartingWithTest -> {
                    try {
                        methodStartingWithTest.invoke(constructor.newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
