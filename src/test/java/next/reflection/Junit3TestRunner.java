package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        Set<Method> testMethods = new LinkedHashSet<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (isTestMethod(method)) {
                testMethods.add(method);
            }
        }

        if(testMethods.size() >= 1) {
            Object instance = clazz.getConstructor().newInstance();

            for (Method method : testMethods) {
                method.invoke(instance);
            }
        }
    }

    public boolean isTestMethod(Method method) {
        return method.getName().startsWith("test");
    }
}
