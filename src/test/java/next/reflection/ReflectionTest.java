package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        for (Field field : clazz.getDeclaredFields()) {
            printPrettified(field);
        }

        logger.info("=======================");

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            printPrettified(constructor);
        }

        logger.info("=======================");

        for (Method method : clazz.getDeclaredMethods()) {
            printPrettified(method);
        }

    }

    private void printPrettified(Field field) {
        logger.debug("Field -------------------------");
        logger.debug("field type: {}", field.getType());
        logger.debug("field name: {}", field.getName());
        logger.debug("modifier  : {}", Modifier.toString(field.getModifiers()));
        logger.debug("-------------------------------");
    }

    private void printPrettified(Constructor<?> constructor) {
        logger.debug("Constructor -------------------");
        final Class[] parameterTypes = constructor.getParameterTypes();
        logger.debug("constructor modifier: {}", Modifier.toString(constructor.getModifiers()));
        logger.debug("param length : {}", parameterTypes.length);
        for (Class paramType : parameterTypes) {
            logger.debug("param type : {}", paramType);
        }
        logger.debug("-------------------------------");
    }

    private void printPrettified(Method method) {
        logger.debug("Method ------------------------");
        logger.debug("method name: {}", method.getName());
        logger.debug("method modifier: {}", Modifier.toString(method.getModifiers()));
        logger.debug("method param count: {}", method.getParameterCount());
        for (Parameter parameter : method.getParameters()) {
            logger.debug("param type: {}", parameter.getType());
            logger.debug("param name: {}", parameter.getName());
        }
        logger.debug("-------------------------------");
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
