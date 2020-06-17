package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        showAllField(clazz);
        showAllConstructor(clazz);
        showAllMethod(clazz);
    }

    private void showAllField(final Class<Question> clazz) {
        logger.debug("Question::fieldName::");
        Arrays.stream(clazz.getDeclaredFields())
                .map(field -> field.getName() + "(type::" + field.getType() + ")")
                .forEach(logger::debug);
    }

    private void showAllConstructor(final Class<Question> clazz) {
        logger.debug("Question::constructor::");
        Arrays.stream(clazz.getDeclaredConstructors())
                .map(constructor -> constructor.getName()
                        + "("
                        + getParameterNameAndType(constructor.getParameters())
                        + ")")
                .forEach(logger::debug);
    }

    private void showAllMethod(final Class<Question> clazz) {
        logger.debug("Question::methods::");
        Arrays.stream(clazz.getDeclaredMethods())
                .map(method -> method.getName()
                        + "(parameters::"
                        + Arrays.toString(method.getParameters())
                        + ", return type::"
                        + method.getReturnType().getName()
                        + ")")
                .forEach(logger::debug);
    }

    private String getParameterNameAndType(final Parameter[] parameters) {
        return Arrays.stream(parameters)
                .map(parameter -> "Name::"
                        + parameter.getName()
                        + " Type::"
                        + parameter.getType().getName())
                .collect(Collectors.joining(", "));
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
