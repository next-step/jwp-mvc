package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    void private_field_access() throws NoSuchMethodException, NoSuchFieldException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
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

    @Test
    @SuppressWarnings("rawtypes")
    void constructor() throws Exception {
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
}
