package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("field 정보를 확인한다.")
    @Test
    void showFields() {
        Class<Question> clazz = Question.class;

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            logger.info("field Name:{}, Type:{}", field.getName(), field.getType());
        }

        List<String> names = Arrays.stream(declaredFields)
                .map(Field::getName)
                .collect(Collectors.toList());
        assertThat(names).contains("questionId", "writer", "title", "contents", "createdDate", "countOfComment");
    }

    @DisplayName("constructor 정보를 확인한다.")
    @Test
    void showConstructors() {
        Class<Question> clazz = Question.class;

        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            logger.info("constructor Name:{}, ParameterTypes:{}", constructor.getName(), constructor.getParameterTypes());
        }
        List<String> names = Arrays.stream(constructors)
                .map(Constructor::getName)
                .collect(Collectors.toList());
        assertThat(names).contains("next.reflection.Question");
    }

    @DisplayName("method 정보를 확인한다.")
    @Test
    void showMethods() {
        Class<Question> clazz = Question.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            logger.info("method Name:{}, ParameterTypes:{}, ReturnType:{}", method.getName(), method.getParameterTypes(), method.getReturnType());
        }

        List<String> names = Arrays.stream(declaredMethods)
                .map(Method::getName)
                .collect(Collectors.toList());
        assertThat(names).contains("update", "getQuestionId", "getWriter", "getCreatedDate", "getTimeFromCreateDate",
                "getCountOfComment", "getTitle", "getContents", "equals", "toString", "hashCode");
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
