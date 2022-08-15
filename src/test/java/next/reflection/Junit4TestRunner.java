package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit4TestRunner {
    @DisplayName("Junit4Test 에서 @MyTest 애노테이션이 있는 메소드를 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test junit4Test = clazz.getConstructor().newInstance();
        List<Method> testMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .collect(Collectors.toList());

        for (Method method : testMethods) {
            method.invoke(junit4Test);
        }
    }
}
