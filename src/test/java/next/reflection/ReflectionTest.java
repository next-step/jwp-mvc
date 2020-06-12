package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("\n\n========== Fields ==========");
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(f -> logger.debug(String.valueOf(f)));

        logger.debug("\n\n========== Constructors ==========");
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(f -> logger.debug(String.valueOf(f)));

        logger.debug("\n\n========== Methods ==========");
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(f -> logger.debug(String.valueOf(f)));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }
}
