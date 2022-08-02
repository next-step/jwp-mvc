package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {

    @Test
    @DisplayName("Junit3Test 클래스에서 test로 시작하는 메서드 실행")
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] methods = clazz.getDeclaredMethods();
        Junit3Test newInstance = clazz.getDeclaredConstructor().newInstance();

        for (Method method : methods) {
            runMethod(newInstance, method);
        }
    }

    private void runMethod(Junit3Test clazz, Method method) throws InvocationTargetException, IllegalAccessException {
        if (method.getName().startsWith("test")) {
            method.invoke(clazz);
        }
    }
}
