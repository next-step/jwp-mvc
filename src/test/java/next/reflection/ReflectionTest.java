package next.reflection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        logger.debug(Arrays.toString(clazz.getDeclaredFields()));
        logger.debug(Arrays.toString(clazz.getDeclaredConstructors()));
        logger.debug(Arrays.toString(clazz.getDeclaredMethods()));
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
        logger.debug(clazz.getName());

        // given
        Student student = new Student();

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");

        // when
        nameField.setAccessible(true);
        ageField.setAccessible(true);

        final String NAME = "민철";
        final int AGE = 30;

        nameField.set(student, NAME);
        ageField.set(student, AGE);

        // then
        assertThat(student.getName()).isEqualTo(NAME);
        assertThat(student.getAge()).isNotEqualTo(29);
        assertThat(student.getAge()).isEqualTo(AGE);
    }

    @Test
    public void constructorWithParameters() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;

        // given
        Constructor<Question> constructorWithArgs = (Constructor<Question>) Arrays.stream(clazz.getConstructors())
                .filter(constructor -> constructor.getParameterTypes().length == 3)
                .findAny()
                .orElseThrow();

        final String writer = "writer";
        final String title = "title";
        final String contents = "contents";

        // when
        Question question = constructorWithArgs.newInstance(writer, title, contents);

        // then
        assertThat(question.getWriter()).isEqualTo(writer);
        assertThat(question.getTitle()).isEqualTo(title);
        assertThat(question.getContents()).isEqualTo(contents);
        assertThat(question).isNotNull();
    }
}