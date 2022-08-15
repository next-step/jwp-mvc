package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        Arrays.stream(clazz.getDeclaredFields()).forEach(System.out::println);
        System.out.println();
        Arrays.stream(clazz.getConstructors()).forEach(System.out::println);
        System.out.println();
        Arrays.stream(clazz.getMethods()).forEach(System.out::println);
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
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);

        Student student = new Student();
        name.set(student, "재성");
        age.set(student, 20);

        assertThat(student.getName()).isEqualTo("재성");
        assertThat(student.getAge()).isEqualTo(20);
    }

}
