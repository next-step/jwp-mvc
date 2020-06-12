package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("\n\n========== Fields ==========");
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(f -> logger.debug(String.valueOf(f)));

        logger.debug("\n\n========== Constructors ==========");
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(f -> logger.debug(String.valueOf(f)));

        logger.debug("\n\n========== Methods ==========");
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(f -> logger.debug(String.valueOf(f)));
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
    @DisplayName("Reflection을 활용하여 Student 클래스의 name, age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인할 수 있다")
    public void privateFieldAcces() throws ReflectiveOperationException {
        // given
        final Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        final Student student = clazz.getConstructor().newInstance();

        final Field nameField = clazz.getDeclaredField("name");
        final Field ageField = clazz.getDeclaredField("age");

        nameField.setAccessible(true);
        ageField.setAccessible(true);

        nameField.set(student, "bactoria");
        ageField.set(student, 28);

        // when
        final String name = student.getName();
        final int age = student.getAge();

        // then
        assertThat(name).isEqualTo("bactoria");
        assertThat(age).isEqualTo(28);
    }

}
