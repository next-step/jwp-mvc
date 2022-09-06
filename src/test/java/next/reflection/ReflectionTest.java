package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("요구사항 1")
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        logger.debug("Fields : {}", Arrays.toString(clazz.getDeclaredFields()));
        logger.debug("Constructors : {}", Arrays.toString(clazz.getDeclaredConstructors()));
        logger.debug("Methods : {}", Arrays.toString(clazz.getDeclaredMethods()));
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
    @DisplayName("요구사항 4")
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student actual = new Student();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(actual, "재영");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(actual, 35);

        logger.debug("student : {}", actual);
        assertThat(actual.getName()).isEqualTo("재영");
        assertThat(actual.getAge()).isEqualTo(35);
    }

    @Test
    @DisplayName("요구사항 5")
    public void initializeInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Question question1 = (Question) constructors[0].newInstance("joel", "test", "clean code");
        Question question2 = (Question) constructors[1].newInstance(1L, "joel", "test", "clean code", new Date(), 1);

        assertThat(question1).isEqualTo(new Question("joel", "test", "clean code"));
        assertThat(question2).isEqualTo(new Question(1L, "joel", "test", "clean code", new Date(), 1));
    }
}
