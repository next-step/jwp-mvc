package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("name: {}", clazz.getName());

        // 클래스의 모든 필드 출력
        for (Field declaredField : clazz.getDeclaredFields()) {
            logger.debug("declaredField: {}", declaredField.getName());
        }

        // 생성자 정보 출력
        for (Constructor<?> constructor : clazz.getConstructors()) {
            logger.debug("constructor {} ", constructor.getName());
        }

        // 메소드에 대한 정보를 출력
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            logger.debug("method {}", declaredMethod.getName());
        }
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
