package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    @DisplayName("test 로 시작하는 메소드 실행")
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            runWithTestMethod(method, clazz);
        }
    }

    private void runWithTestMethod(Method method, Class<Junit3Test> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (method.getName().startsWith("test")) {
            method.invoke(clazz.newInstance());
        }
    }
}
