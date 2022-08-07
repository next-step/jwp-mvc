package next.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Junit3TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit3TestRunner.class);

    @Test
    void run() {
        Class<Junit3Test> clazz = Junit3Test.class;
        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
            .filter(method -> method.getName().startsWith("test"))
            .forEach(method -> {
                try {
                    method.invoke(clazz.newInstance());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            });
    }
}
