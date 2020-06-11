package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {
    private static final Logger log = LoggerFactory.getLogger(Junit4TestRunner.class);

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
            .forEach(method -> invokeMethod(junit4Test, method));
    }

    private Object invokeMethod(Junit4Test Junit4Test, Method method) {
        try {
            return method.invoke(Junit4Test);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
