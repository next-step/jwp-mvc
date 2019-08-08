package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        for (Field field : clazz.getDeclaredFields()) {
            logger.debug("field : {}", field);
        }

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            logger.debug("constructor : {}", constructor);
        }

        for (Method method : clazz.getDeclaredMethods()) {
            logger.debug("method : {}", method);
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
        Student student = clazz.newInstance();
        String nameValue = "name!!!!!!!";
        int ageValue = 999999;

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, nameValue);

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.setInt(student, ageValue);

        assertThat(student.getName()).isEqualTo(nameValue);
        assertThat(student.getAge()).isEqualTo(ageValue);
    }
}
