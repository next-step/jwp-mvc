package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit3TestRunner {

    private static final String METHOD_PREFIX_FOR_EXECUTING_TEST = "test";

    @DisplayName("Junit3에서는 메소드 이름이 'test'로 시작해야 테스트를 자동 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        List<Method> testMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::hasTestingPrefix)
                .collect(Collectors.toList());

        Junit3Test testClassInstance = clazz.newInstance();
        for (Method testMethod : testMethods) {
            testMethod.invoke(testClassInstance);
        }
    }

    private boolean hasTestingPrefix(Method method) {
        String methodName = method.getName();
        return methodName.startsWith(METHOD_PREFIX_FOR_EXECUTING_TEST);
    }
}
