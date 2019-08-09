package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Date;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        logger.debug("Question name: {}", clazz.getName());
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> logger.debug("Field : {}", field));
        Arrays.stream(clazz.getConstructors()).forEach(constructor -> logger.debug(constructor.toString()));
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> logger.debug("Method : {}", String.valueOf(method)));
    }

    @Test
    public void privateFieldAccess() throws IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.getName().equals("name")) {
                field.set(new Student(), "이름");
            } else {
                field.setInt(new Student(), 16);
                logger.debug("field : {}", field);
            }
        }
    }


    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() {
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
