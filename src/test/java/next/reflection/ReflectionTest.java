package next.reflection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();

        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            log.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                log.debug("param type : {}", paramType);
            }
        }
    }
}
