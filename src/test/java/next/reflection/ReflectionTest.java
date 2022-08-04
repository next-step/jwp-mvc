package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void showClass() {
        //given
        Class<Question> questionClass = Question.class;
        //when, then
        assertAll(
                () -> assertThat(questionClass.getDeclaredFields())
                        .extracting(Field::getName)
                        .containsExactlyInAnyOrder("questionId", "writer", "title", "contents", "createdDate", "countOfComment"),
                () -> assertThat(questionClass.getDeclaredConstructors())
                        .extracting(Constructor::getName)
                        .containsExactlyInAnyOrder("next.reflection.Question", "next.reflection.Question"),
                () -> assertThat(questionClass.getDeclaredMethods())
                        .extracting(Method::getName)
                        .containsExactlyInAnyOrder(
                                "getQuestionId", "getWriter", "getTitle", "getContents", "getCreatedDate",
                                "getTimeFromCreateDate", "getCountOfComment", "update", "toString", "hashCode", "equals"
                        )
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
