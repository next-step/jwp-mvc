package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("getDeclaredFields() \n");
        for (Field field : clazz.getDeclaredFields()) {
            logger.debug(field.toString());
        }

        logger.debug("getDeclaredConstructors() \n");
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            logger.debug(constructor.toString());
        }

        logger.debug("getDeclaredMethods() \n");
        for (Method method : clazz.getDeclaredMethods()) {
            logger.debug(method.toString());
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
    void accessPrivateField() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;

        String name = "YD";
        int age = 27;

        setAccessibleField(clazz, "name");
        setAccessibleField(clazz, "age");

        Student student = new Student();

        setValue(clazz, student, "name", name);
        setValue(clazz, student, "age", age);

        assertAll(
                () -> assertThat(student.getName()).isEqualTo(name),
                () -> assertThat(student.getAge()).isEqualTo(age)
        );
    }

    private void setAccessibleField(Class<Student> clazz, String fieldName) throws NoSuchFieldException {
        Field target = clazz.getDeclaredField(fieldName);
        target.setAccessible(true);
    }

    private void setValue(Class<Student> clazz, Object instance, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field target = clazz.getDeclaredField(fieldName);
        target.setAccessible(true);

        target.set(instance, value);
    }
}
