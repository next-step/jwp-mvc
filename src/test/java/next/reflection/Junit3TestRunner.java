package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Junit3TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test junit3Test = clazz.getConstructor().newInstance();

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    try {
                        method.invoke(junit3Test);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error(e.getMessage());
                    }
                });
    }
}
