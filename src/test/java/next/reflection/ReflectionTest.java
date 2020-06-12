package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("class name : " + clazz.getName());
        logger.debug("class access modifier : " + Modifier.toString(clazz.getModifiers()));
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            logger.debug("field : " + Modifier.toString(field.getModifiers()) + " " + field.getName());
        });
        Arrays.stream(clazz.getDeclaredConstructors()).forEach(constructor -> {
            logger.debug("constructor : " + Modifier.toString(constructor.getModifiers()) + " " + constructor.getName());
        });
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
            logger.debug("method : " + Modifier.toString(method.getModifiers()) + " " + method.getName());
        });
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
