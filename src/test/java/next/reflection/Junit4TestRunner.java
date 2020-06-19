package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit4TestRunner {

    private static final Class<MyTest> ANNOTATION_FOR_EXECUTING_TEST = MyTest.class;

    @DisplayName("Junit4에서는 어노테이션을 기준으로 테스트를 자동 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        List<Method> testMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::hasTestingAnnotation)
                .collect(Collectors.toList());

        Junit4Test testClassInstance = clazz.newInstance();
        for (Method testMethod : testMethods) {
            testMethod.invoke(testClassInstance);
        }
    }

    private boolean hasTestingAnnotation(Method method) {
        return method.isAnnotationPresent(ANNOTATION_FOR_EXECUTING_TEST);
    }
}
