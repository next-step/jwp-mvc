package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Question 객체의 모든 필드, 생성자, 메소드를 출력")
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        logger.debug("Fields: {}",
                Arrays.stream(clazz.getDeclaredFields())
                        .map(field -> String.valueOf(field))
                        .collect(Collectors.joining(", ")));
        logger.debug("Constructors : {}",
                Arrays.stream(clazz.getDeclaredConstructors())
                        .map(constructor -> String.valueOf(constructor))
                        .collect(Collectors.joining(", ")));
        logger.debug("Methods : {}",
                Arrays.stream(clazz.getDeclaredMethods())
                        .map(method -> String.valueOf(method))
                        .collect(Collectors.joining(", ")));
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
    @DisplayName("private 필드에 접근하기")
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

    @DisplayName("Question 객체 생성자에 직접 Reflection 하는 테스트")
    @ParameterizedTest
    @MethodSource("questionGenerator")
    public void initializeInstance(Question question, int i) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Question question1 = (Question) constructors[0].newInstance("joel", "test", "clean code");
        Question question2 = (Question) constructors[1].newInstance(1L, "joel", "test", "clean code", new Date(), 1);
        if (i == 0) {
            assertThat(question1).isEqualTo(question);
        } else {
            assertThat(question2).isEqualTo(question);
        }
    }

    private static Stream<Arguments> questionGenerator() {
        return Stream.of(
                Arguments.of(new Question("joel", "test", "clean code"), 0),
                Arguments.of(new Question(1L, "joel", "test", "clean code", new Date(), 1), 1)
        );
    }
}
