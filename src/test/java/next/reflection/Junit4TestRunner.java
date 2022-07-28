package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit4TestRunner {

    @Test
    @DisplayName("@MyTest 애노테이션이 있는 메소드 실행")
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        for (Method method : clazz.getDeclaredMethods()) {
            runningMyTestAnnotation(clazz, method);
        }

    }

    private void runningMyTestAnnotation(Class<Junit4Test> clazz, Method method) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (method.isAnnotationPresent(MyTest.class)) {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        }
    }
}
