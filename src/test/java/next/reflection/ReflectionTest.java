package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("Reflection - 모든 Fields 반환")
    @Test
    public void showClass_Fields() {
        //given
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        //when
        Field[] declaredFields = clazz.getDeclaredFields();

        //then
        assertThat(declaredFields.length).isEqualTo(6);
        IntStream.range(0, declaredFields.length)
                .forEach(i -> System.out.println("Field : " + declaredFields[i]));
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
