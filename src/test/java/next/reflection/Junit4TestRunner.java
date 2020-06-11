package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행

        Set<Method> testMethods = new LinkedHashSet<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (isTestMethod(method)) {
                testMethods.add(method);
            }
        }

        if (testMethods.size() >= 1) {
            Object instance = clazz.getConstructor().newInstance();

            for (Method method : testMethods) {
                method.invoke(instance);
            }
        }
    }

    public boolean isTestMethod(Method method) {
        return method.isAnnotationPresent(MyTest.class);
    }
}
