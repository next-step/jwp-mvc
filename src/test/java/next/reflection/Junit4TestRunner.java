package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Junit4TestRunner {

    private static final Class<MyTest> TEST_ANNOTATION = MyTest.class;

    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행

        List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(TEST_ANNOTATION))
                .collect(Collectors.toList());

        Object testInatance = clazz.newInstance();
        for(Method method : methods) {
            method.invoke(testInatance);
        }
    }
}
