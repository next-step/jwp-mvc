package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            logger.debug("field name : {}, field type : {}", field.getName(), field.getType());
        }

        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("constructor paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("constructor param type : {}", paramType);
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            logger.debug("method name : {}", method.getName());
            Class[] parameterTypes = method.getParameterTypes();
            logger.debug("method parameter length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("method param type : {}", paramType);
            }
            logger.debug("method return type : {}", method.getReturnType());
        }
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
