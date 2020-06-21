package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private Class<Question> clazz = Question.class;

    @Test
    public void showClass() {
        logger.debug(clazz.getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void showConstructor() {
        Constructor[] constructors = clazz.getConstructors();
        Arrays.stream(constructors)
                .forEach(constructor -> logger.debug("parameter count : {}, paramter type : {}",
                        constructor.getParameterCount(), constructor.getParameterTypes()));
    }

    @Test
    void showFields() {
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> logger.debug("filed name : {}, field type : {}", field.getName(), field.getType()));
    }

    @Test
    void showMethod() {
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> logger.debug("method name : {}, parameter count : {}, parameter type : {}, return type : {}",
                        method.getName(), method.getParameterCount(), method.getParameterTypes(), method.getReturnType()));
    }

    @Test
    void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        Field name = clazz.getDeclaredField("name");
        Field age = clazz.getDeclaredField("age");
        name.setAccessible(true);
        age.setAccessible(true);

        Student student = new Student();
        name.set(student, "dowon");
        age.setInt(student, 27);

        assertThat(student.getName())
                .isEqualTo("dowon");
        assertThat(student.getAge())
                .isEqualTo(27);
    }

    @Test
    void createInstance() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<Question> constructor = clazz.getConstructor(String.class, String.class, String.class);
        Question question = constructor.newInstance("writer", "title", "contents");

        assertThat(question)
                .isEqualTo(new Question("writer", "title", "contents"));
    }
}
