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
        // 필드
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {logger.debug("Field : {}", field);});
        // 생성자
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> {logger.debug("Constructor : {}", constructor);});
        // 메소드
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {logger.debug("Method : {}", method);});
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
