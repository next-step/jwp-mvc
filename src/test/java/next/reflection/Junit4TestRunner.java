package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {

    @Test
    @DisplayName("MyTest 어노테이션이 있는 메소드만 실행하는 테스트")
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        invokeMethods(
            clazz.newInstance(),
            clazz.getDeclaredMethods()
        );
    }

    private void invokeMethods(Junit4Test junit4Test, Method[] methods) {
        if (methods.length <= 0) {
            return;
        }

        Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(MyTest.class))
            .forEach(method -> {
                try {
                    method.invoke(junit4Test);
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
    }
}
