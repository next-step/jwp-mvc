package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;

@DisplayName("Reflection 테스트")
public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> logger.debug("Class 모든 필드 : {}", field));

        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> logger.debug("Class의 모든 생성자 : {}", constructor));

        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> logger.debug("Class의 모든 메소드 : {}", method));
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
