package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        for (Field field : clazz.getFields()) {
            logger.info("[Question - All Field]: {}", field);
        }

        for (Constructor<?> constructor : clazz.getConstructors()) {
            logger.info("[Question - All Constructor]: {}", constructor);
        }

        for (Method method : clazz.getMethods()) {
            logger.info("[Question - All Method]: {}", method);
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

    @DisplayName("Student class에서 private field에 (name, age) 값 할당")
    @Test
    public void privateFieldAccess() throws Exception {
        Student student = new Student();
        Class<Student> clazz = Student.class;

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "죠르디");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 20);

        assertThat(student.getName()).isEqualTo("죠르디");
        assertThat(student.getAge()).isEqualTo(20);
    }
}
