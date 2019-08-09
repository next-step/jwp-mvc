package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.stream.Stream;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("* field list");
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            logger.debug(field.getName());
        }

        logger.debug("* constructor list");
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            logger.debug(constructor.getName());
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
    @SuppressWarnings("rawtypes")
    public void field() throws Exception {
        Class<Question> clazz = Question.class;

        logger.debug("* public field list");
        Field[] publicFields = clazz.getFields();
        for (Field field : publicFields) {
            logger.debug("\t\t{} {}", Modifier.toString(field.getModifiers()), field.getName());
        }

        logger.debug("* all field list");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            logger.debug("\t\t{} {}", Modifier.toString(field.getModifiers()), field.getName());
        }
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void method() throws Exception {
        Class<Question> clazz = Question.class;

        logger.debug("* public field list");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            logger.debug("\t\t{} {}", Modifier.toString(method.getModifiers()), method.getName());
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                logger.debug("\t\t\t{} {}", Modifier.toString(parameter.getModifiers()), parameter.getType().getName());
            }
        }
    }
}
