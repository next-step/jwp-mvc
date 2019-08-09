package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class Junit4TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit4TestRunner.class);
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> methodInvoke(clazz, method));
    }

    private void methodInvoke(Class<Junit4Test> clazz, Method method) {
        try {
            method.invoke(clazz.newInstance());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
