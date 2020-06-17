package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void privateFieldAccess() throws Exception {
        final Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        final Student student = new Student();
        final Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "jinwoo");

        final Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 30);

        assertThat(student.getName()).isEqualTo("jinwoo");
        assertThat(student.getAge()).isEqualTo(30);
    }

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        showAllField(clazz);
        showAllConstructor(clazz);
        showAllMethod(clazz);
    }

    private void showAllField(final Class<Question> clazz) {
        logger.debug("Question::fieldName::");
        Arrays.stream(clazz.getDeclaredFields())
                .map(field -> field.getName() + "(type::" + field.getType() + ")")
                .forEach(logger::debug);
    }

    private void showAllConstructor(final Class<Question> clazz) {
        logger.debug("Question::constructor::");
        Arrays.stream(clazz.getDeclaredConstructors())
                .map(constructor -> constructor.getName()
                        + "("
                        + getParameterNameAndType(constructor.getParameters())
                        + ")")
                .forEach(logger::debug);
    }

    private void showAllMethod(final Class<Question> clazz) {
        logger.debug("Question::methods::");
        Arrays.stream(clazz.getDeclaredMethods())
                .map(method -> method.getName()
                        + "(parameters::"
                        + Arrays.toString(method.getParameters())
                        + ", return type::"
                        + method.getReturnType().getName()
                        + ")")
                .forEach(logger::debug);
    }

    private String getParameterNameAndType(final Parameter[] parameters) {
        return Arrays.stream(parameters)
                .map(parameter -> "Name::"
                        + parameter.getName()
                        + " Type::"
                        + parameter.getType().getName())
                .collect(Collectors.joining(", "));
    }
    @Test
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<Question> constructor = clazz.getConstructor(String.class, String.class, String.class);
        Question question = constructor.newInstance("jinwoo", "title", "Hello World");
        logger.debug(question.toString());

        Constructor<Question> constructor1 = clazz.getConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);
        Question question1 = constructor1.newInstance(1L, "jw", "title2", "Hello World", new Date(), 0);
        logger.debug(question1.toString());
    }
}
