package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            runWithTestMethod(method, clazz);
        }
    }

    private void runWithTestMethod(Method method, Class<Junit4Test> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (method.isAnnotationPresent(MyTest.class)) {
            method.invoke(clazz.newInstance());
        }
    }
}
