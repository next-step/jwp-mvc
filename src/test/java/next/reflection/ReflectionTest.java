package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        showAll(clazz.getDeclaredFields());
        showAll(clazz.getDeclaredConstructors());
        showAll(clazz.getMethods());
    }

    public <T extends Member> void showAll(T[] elements) {
        Arrays.stream(elements)
                .forEach(e -> logger.debug(e.getName()));
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
