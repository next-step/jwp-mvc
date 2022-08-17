package next.reflection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("Question 클래스의 모든 필드, 생성자, 메서드 정보 확인")
    @Test
    public void show_class() {
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
}
