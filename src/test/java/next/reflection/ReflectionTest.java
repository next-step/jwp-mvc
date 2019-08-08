package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("=========== name: " + clazz.getName());
        logger.debug("=========== fields: " + Arrays.toString(clazz.getDeclaredFields()));
        logger.debug("=========== constructors: " + Arrays.toString(clazz.getDeclaredConstructors()));
        logger.debug("=========== methods: " + Arrays.toString(clazz.getDeclaredMethods()));
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
        Map<String, Object> fieldValue = new HashMap<>();

        fieldValue.put("name", "장소현");
        fieldValue.put("age", 28);

        Class<Student> clazz = Student.class;
        Student student = new Student();
        logger.debug(clazz.getName());
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            field.set(student, fieldValue.get(field.getName()));
        }

        assertThat(student.getName()).isEqualTo("장소현");
        assertThat(student.getAge()).isEqualTo(28);
    }
}