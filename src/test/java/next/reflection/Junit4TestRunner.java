package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Junit4TestRunner {
    @Test
    @DisplayName("@MyTest 어노테이션 사용 메서드 실행")
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        Junit4Test newInstance = clazz.getDeclaredConstructor().newInstance();

        for (Method method : methods) {
            runMethod(newInstance, method);
        }
    }

    private void runMethod(Junit4Test clazz, Method method) throws InvocationTargetException, IllegalAccessException {
        if (method.isAnnotationPresent(MyTest.class)) {
            method.invoke(clazz);
        }
    }
}
