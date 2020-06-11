package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Junit4TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit4TestRunner.class);

    @Test
    @DisplayName("@MyTest 어노테이션이 붙어있는 메소드만 실행시키기")
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // Junit4Test 에서 @MyTest 애노테이션이 있는 메소드 실행

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class)) // filter method that has MyTest annotation
                .forEach(method -> runMethod(method, clazz));
    }

    private void runMethod(final Method method, final Class<Junit4Test> clazz) {
        try {
            method.invoke(clazz.newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            logger.error("Fail to run method {}", method.getName());
        }
    }
}
