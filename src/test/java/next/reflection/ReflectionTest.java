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
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    static Stream<Arguments> keyAndValue() {
        return Stream.of(
                Arguments.of("name", "jinwoo"),
                Arguments.of("age", 30)
        );
    }

    @ParameterizedTest
    @DisplayName("private 필드에 직접 값을 할당하여 테스트를 진행")
    @MethodSource(value = "keyAndValue")
    public void privateFieldAccess(final String fieldName, final Object fieldValue) throws Exception {
        final Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        final Student student = new Student();
        final Field name = clazz.getDeclaredField(fieldName);
        name.setAccessible(true);
        name.set(student, fieldValue);

        final String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        final Method getterMethod = clazz.getDeclaredMethod(methodName);
        final Object result = getterMethod.invoke(student);
        assertThat(result).isEqualTo(fieldValue);
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
    public void constructor() {
        Class<Question> clazz = Question.class;
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }
}
