package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit3TestRunner {
    @DisplayName("Junit3Test 에서 test 로 시작하는 메소드를 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.getConstructor().newInstance();
        List<Method> testMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());

        for (Method method : testMethods) {
            method.invoke(junit3Test);
        }
    }
}
