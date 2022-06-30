package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {

    @DisplayName("Junit3Test 에서 test 로 시작하는 메소드들을 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            invokeStartsWithTest(method, clazz);
        }
    }

    private void invokeStartsWithTest(Method method, Class clazz) {
        if (method.getName().startsWith("test")) {
            invoke(method, clazz);
        }
    }

    private void invoke(Method method, Class clazz) {
        try {
            method.invoke(clazz.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
