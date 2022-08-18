package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("Question 클래스의 모든 필드, 생성자, 메서드 정보 확인")
    @Test
    void show_class() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        assertAll(
                () -> assertThat(clazz.getDeclaredFields())
                        .extracting(Field::getName)
                        .containsExactlyInAnyOrder("questionId", "writer", "title", "contents",
                                "createdDate" ,"countOfComment"),
                () -> assertThat(clazz.getDeclaredConstructors())
                        .hasSize(2),
                () -> assertThat(clazz.getDeclaredMethods())
                        .extracting(Method::getName)
                        .containsExactlyInAnyOrder("getQuestionId", "getWriter", "getTitle", "getContents",
                                "getCreatedDate", "getTimeFromCreateDate", "getCountOfComment", "update",
                                "toString", "hashCode", "equals")

        );
    }

    @DisplayName("private field에 값 할당")
    @Test
    void private_field_access() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        Student student = clazz.getConstructor().newInstance();

        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(student, "산하");

        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        ageField.set(student, 25);

        assertAll(
                () -> assertThat(clazz.getDeclaredMethod("getName").invoke(student)).isEqualTo("산하"),
                () -> assertThat(clazz.getDeclaredMethod("getAge").invoke(student)).isEqualTo(25)
        );
    }

    @DisplayName("인자를 가진 Question 클래스의 인스턴스를 생성")
    @Test
    @SuppressWarnings("rawtypes")
    void init_question() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<?> targetConstructor = Stream.of(clazz.getDeclaredConstructors())
                .filter(constructor -> Arrays.asList(constructor.getParameterTypes())
                        .equals(Arrays.asList(String.class, String.class, String.class)))
                .findAny()
                .get();

        Object target = targetConstructor.newInstance("박재성", "NextStep", "JAVA");

        assertThat(target).isEqualTo(new Question("박재성", "NextStep", "JAVA"));
    }
}
