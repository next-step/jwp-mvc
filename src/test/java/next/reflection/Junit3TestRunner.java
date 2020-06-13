package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {
    private static final Logger log = LoggerFactory.getLogger(Junit3TestRunner.class);

    private final String INVOCABLE_METHODS_PREFIX = "test";

    @Test
    @DisplayName("test로 시작하는 메소드만 실행하는 테스트")
    public void run() throws IllegalAccessException, InstantiationException {
        Class<Junit3Test> clazz = Junit3Test.class;

        invokeMethods(
            clazz.newInstance(),
            clazz.getDeclaredMethods()
        );
    }

    private void invokeMethods(Junit3Test junit3Test, Method[] methods) {
        if (methods.length <= 0) {
            return;
        }

        Arrays.stream(methods)
            .filter(method -> method.getName().startsWith(INVOCABLE_METHODS_PREFIX))
            .forEach(method -> invokeMethod(junit3Test, method));
    }

    private Object invokeMethod(Junit3Test junit3Test, Method method) {
        try {
            return method.invoke(junit3Test);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
