package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit3TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit3TestRunner.class);

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // Junit3Test 에서 test 로 시작하는 메소드 실행
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test")) // filter that method name start with test
                .forEach(method -> runMethod(method, clazz));
    }

    private void runMethod(Method method, Class<Junit3Test> clazz) {
        try {
            method.invoke(clazz.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            logger.error("Fail to invoke method {}", method.getName());
        }
    }
}
