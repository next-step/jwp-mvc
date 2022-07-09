package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    // 필드, 메서드, 생성자를 출력한다.
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("start logging declared fields");
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> logger.debug(field.getName()));

        logger.debug("start logging declared methods");
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> logger.debug(method.getName()));

        logger.debug("start logging declared constructors");
        Arrays.stream(clazz.getDeclaredConstructors()).forEach(field -> logger.debug(field.getName()));
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
