package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        for (final Constructor constructor : clazz.getDeclaredConstructors()) {
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
            field.set(object, getMockValue(field.getType()));
        }

        for (final Method method : clazz.getMethods()) {
            if (!method.getName().startsWith("get")) {
                continue;
            }

            final Object fieldValue = method.invoke(object);
            logger.debug("{}: {}", method.getName(), fieldValue);
        }
    }

    @Test
    void newQuestion() throws Exception {
        Class<Question> clazz = Question.class;

        for (final Constructor constructor : clazz.getDeclaredConstructors()) {
            final Object[] parameters = Arrays.stream(constructor.getParameterTypes())
                    .map(this::getMockValue)
                    .toArray();

            final Object object = constructor.newInstance(parameters);

            logger.debug("{}", object);
        }
    }

    private Object getMockValue(final Class type) {
        final String fieldType = type.getSimpleName().toLowerCase();
        if ("string".equals(fieldType)) {
            return "StringValue";
        }
        if ("int".equals(fieldType)) {
            return 100;
        }
        if ("long".equals(fieldType)) {
            return 100_000_000;
        }
        if ("date".equals(fieldType)) {
            return new Date();
        }

        return null;
    }
}

