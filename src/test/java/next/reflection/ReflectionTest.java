package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        assertThat(clazz.getName())
                .as("String getName() : 패키지 + 클래스 이름을 반환한다.")
                .isEqualTo("next.reflection.Question");

        assertThat(clazz.getModifiers())
                .as("int getModifiers() : 클래스의 접근 제어자를 숫자로 반환한다.")
                .isEqualTo(1);

        assertThat(clazz.getFields())
                .as("Field[] getFields() : 접근 가능한 public 필드 목록을 반환한다.")
                .isEmpty();

        assertThat(clazz.getDeclaredFields().length)
                .as("Field[] getDeclaredFields() : 모든 필드 목록을 반환한다.")
                .isEqualTo(6);

        assertThat(clazz.getConstructors().length)
                .as("Constructor[] getConstructors() : 접근 가능한 public 생성자 목록을 반환한다.")
                .isEqualTo(2);

        assertThat(clazz.getDeclaredConstructors().length)
                .as("Constructor[] getDeclaredConstructors() : 모든 생성자 목록을 반환한다.")
                .isEqualTo(2);

        assertThat(clazz.getMethods().length)
                .as("Method[] getMethods() : 부모 클래스, 자신 클래스의 접근 가능한 public 메서드 목록을 반환한다.")
                .isEqualTo(17);

        assertThat(clazz.getDeclaredMethods().length)
                .as("Method[] getDeclaredMethods() : 모든 메서드 목록을 반환한다.")
                .isEqualTo(11);
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
