package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Junit4TestRunner {

    @DisplayName("Junit4Test 에서 @MyTest 애노테이션이 있는 메소드를 실행한다.")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        for (Method method : clazz.getDeclaredMethods()) {
            invokeWithAnnotation(method, clazz, MyTest.class);
        }
    }

    private void invokeWithAnnotation(Method method, Class clazz, Class annotation) {
        if (method.isAnnotationPresent(annotation)) {
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
