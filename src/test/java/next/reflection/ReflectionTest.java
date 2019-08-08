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
        for (final Constructor constructor : clazz.getConstructors()) {
            logger.debug("\tconstructor: {}", constructor);

            for (final Class parameterType : constructor.getParameterTypes()) {
                logger.debug("\t\tparameter: {}", parameterType);
            }
        }
        for (final Method method : clazz.getMethods()) {
            logger.debug("\tmethod: {}", method);

            for (final Class parameterType : method.getParameterTypes()) {
                logger.debug("\t\tparameter: {}", parameterType);
            }
        }
        for (final Field field : clazz.getDeclaredFields()) {
            logger.debug("\tfield: {}", field);
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

    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        final Object object = clazz.getConstructor().newInstance();
        for (final Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            final String fieldType = field.getType().getSimpleName();
            if ("String".equals(fieldType)) {
                field.set(object, "StringValue");
            }
            if ("int".equals(fieldType)) {
                field.set(object, 100);
            }
        }

        for (final Method method : clazz.getMethods()) {
            if (!method.getName().startsWith("get")) {
                continue;
            }

            final Object fieldValue = method.invoke(object);
            logger.debug("{}: {}", method.getName(), fieldValue);
        }
    }
}
