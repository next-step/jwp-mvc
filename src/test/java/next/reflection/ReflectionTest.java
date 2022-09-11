package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("fields : {}", Arrays.toString(clazz.getDeclaredFields()));
        logger.debug("constructors: {}", Arrays.toString(clazz.getDeclaredConstructors()));
        logger.debug("methods: {}", Arrays.toString(clazz.getDeclaredMethods()));
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
    public void privateFieldAccess() throws IllegalAccessException, NoSuchFieldException {
        Class<Student> clazz = Student.class;
        final Student student = new Student();

        final Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(student, "지원");
        final Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        ageField.set(student, 25);

        assertEquals("지원", student.getName());
        assertEquals(25, student.getAge());
    }
}
