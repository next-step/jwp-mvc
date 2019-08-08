package next.reflection;

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

        List<String> declaredFields = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
        assertThat(declaredFields)
                .as("Field[] getDeclaredFields() : 모든 필드 목록을 반환한다.")
                .hasSize(6)
                .containsOnly("questionId", "writer", "title", "contents", "createdDate", "countOfComment");

        List<String> constructors = Arrays.stream(clazz.getConstructors())
                .map(Constructor::getName)
                .collect(Collectors.toList());
        assertThat(constructors)
                .as("Constructor[] getConstructors() : 접근 가능한 public 생성자 목록을 반환한다.")
                .hasSize(2)
                .containsOnly("next.reflection.Question", "next.reflection.Question");

        List<String> declaredConstructors = Arrays.stream(clazz.getDeclaredConstructors())
                .map(Constructor::getName)
                .collect(Collectors.toList());
        assertThat(declaredConstructors)
                .as("Constructor[] getDeclaredConstructors() : 모든 생성자 목록을 반환한다.")
                .hasSize(2)
                .containsOnly("next.reflection.Question", "next.reflection.Question");

        List<String> methods = Arrays.stream(clazz.getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());
        assertThat(methods)
                .as("Method[] getMethods() : 부모 클래스, 자신 클래스의 접근 가능한 public 메서드 목록을 반환한다.")
                .hasSize(17)
                .containsOnly(
                        "equals",
                        "toString",
                        "hashCode",
                        "update",
                        "getQuestionId",
                        "getWriter",
                        "getTitle",
                        "getCreatedDate",
                        "getTimeFromCreateDate",
                        "getCountOfComment",
                        "getContents",
                        "wait",
                        "wait",
                        "wait",
                        "getClass",
                        "notify",
                        "notifyAll");

        List<String> declaredMethods = Arrays.stream(clazz.getDeclaredMethods())
                .map(Method::getName)
                .collect(Collectors.toList());
        assertThat(declaredMethods)
                .as("Method[] getDeclaredMethods() : 상속 제외한 자신의 모든 메서드 목록을 반환한다.")
                .hasSize(11)
                .containsOnly(
                        "equals",
                        "toString",
                        "hashCode",
                        "update",
                        "getTitle",
                        "getQuestionId",
                        "getWriter",
                        "getCreatedDate",
                        "getTimeFromCreateDate",
                        "getCountOfComment",
                        "getContents");
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
