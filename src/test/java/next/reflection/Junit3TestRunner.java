package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3TestRunner {

    @Test
    @DisplayName("Junit3Test에서 test로 시작하는 메소드 실행")
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            startMethodNameIsTest(clazz, method);
        }
    }

    private void startMethodNameIsTest(Class<Junit3Test> clazz, Method method) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (method.getName().startsWith("test")) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        }
    }
}
