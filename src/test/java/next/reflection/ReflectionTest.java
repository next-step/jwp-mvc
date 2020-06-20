package next.reflection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ReflectionTest {

    @DisplayName("Class의 필드, 생성자, 메소드 정보 보여주기")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        log.debug(clazz.getName());

        for (Field field : clazz.getDeclaredFields()) {
            log.debug("field : name={}, type={}", field.getName(), field.getType());
        }

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            log.debug("constructor : name={}", constructor.getName());

            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                log.debug("constructor parameterType : name={}, type={}", parameterType.getName(), parameterType.getTypeName());
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            log.debug("method : name={}", method.getName());

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                log.debug("method parameterType : name={}, type={}", parameterType.getName(), parameterType.getTypeName());
            }

            Class<?> returnType = method.getReturnType();
            log.debug("method returnType : type={}", returnType.getName());
        }
    }

    @DisplayName("private Field에 값 할당하기")
    @Test
    void privateFieldAccess() throws Exception {
        /* given */
        Student student = new Student();

        String expectedName = "luke";
        int expectedAge = 20;

        /* when */
        setFieldToInstance(student, "name", expectedName);
        setFieldToInstance(student, "age", expectedAge);

        /* then */
        assertThat(student.getName()).isEqualTo(expectedName);
        assertThat(student.getAge()).isEqualTo(expectedAge);
    }

    @DisplayName("인자를 가진 Question 클래스의 인스턴스 생성하기")
    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getDeclaredConstructors();

        Object instance;
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            log.debug("paramer length : {}", parameterTypes.length);

            for (Class paramType : parameterTypes) {
                log.debug("param type : {}", paramType);
            }

            if (parameterTypes.length == 3) {
                instance = constructor.newInstance("testWriter", "testTitle", "testContents");
                log.debug("instance : {}", instance);
            }

            if (parameterTypes.length == 6) {
                instance = constructor.newInstance(1L, "testWriter", "testTitle", "testContents", new Date(), 0);
                log.debug("instance : {}", instance);
            }
        }
    }

    private <T> void setFieldToInstance(T instance, String fieldName, Object fieldValue) throws Exception {
        Class<?> clazz = instance.getClass();

        Field field = clazz.getDeclaredField(fieldName);

        field.setAccessible(true);
        field.set(instance, fieldValue);
    }
}
