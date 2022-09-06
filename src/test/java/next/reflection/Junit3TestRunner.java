package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            runWithTestMethod(method);
        }
    }

    private void runWithTestMethod(Method method) throws InvocationTargetException, IllegalAccessException {
        if (method.getName().startsWith("test")) {
            method.invoke(new Junit3Test());
        }
    }
}
