package next.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        Field[] fields = clazz.getDeclaredFields();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Method[] methods = clazz.getDeclaredMethods();

        logger.debug(clazz.getName());

        // Field
        Arrays.stream(fields).forEach(
            field -> logger.debug("Field Name: " + Modifier.toString(field.getModifiers()) + " " + field.getName())
        );

        // Constructor
        Arrays.stream(constructors).forEach(
            constructor -> {
                logger.debug("Constructor Name: " + Modifier.toString(constructor.getModifiers()) + " " +  constructor.getName());
                Arrays.stream(constructor.getParameterTypes()).forEach(parameter ->
                    logger.debug("Constructor " + constructor.getName() + "  Parameter Type: "
                    + Modifier.toString(parameter.getModifiers()) + " " +  parameter.getName()));
            }
        );

        // Method
        Arrays.stream(methods).forEach(
            method -> {
                logger.debug("Method Name: " + Modifier.toString(method.getModifiers()) + " " + method.getName());
                Arrays.stream(method.getParameters()).forEach(parameter ->
                    logger.debug("Method " + method.getName() + "  Parameter Type: "
                    + Modifier.toString(parameter.getModifiers()) + " " +  parameter.getName()));
            }

        );

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
