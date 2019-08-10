package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class Junit3TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit3TestRunner.class);
    private static final String TEST_PREFIX = "test";

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith(TEST_PREFIX))
                .forEach(method -> methodInvoke(clazz, method));
    }

    private void methodInvoke(Class<Junit3Test> clazz, Method method) {
        try {
            method.invoke(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
