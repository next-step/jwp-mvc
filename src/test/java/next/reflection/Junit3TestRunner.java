package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit3TestRunner {

    private static final String TEST_PREFIX = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith(TEST_PREFIX))
                .collect(Collectors.toList());

        Object testInatance = clazz.newInstance();
        for(Method method : methods) {
            method.invoke(testInatance);
        }


    }
}
