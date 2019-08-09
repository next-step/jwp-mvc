package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class Junit3TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(Junit3TestRunner.class);

    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        Junit3Test junit3Test = clazz.newInstance();
        Stream.of(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> {
                    logger.debug("method name: {}", method.getName());
                    try {
                        method.invoke(junit3Test);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("error : {}", method.getName(), e);
                    }
                });
    }
}
