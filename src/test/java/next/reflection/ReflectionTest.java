package next.reflection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    private static Class<Question> targetClass;

    @BeforeAll
    public static void init() {
        targetClass = Question.class;
    }

    @DisplayName("reflection 테스트 : 클래스 ")
    @Test
    public void showClass() {
        logger.debug(targetClass.getName());
    }

    @DisplayName("reflection 테스트 : 생성자 ")
    @Test
    @SuppressWarnings("rawtypes")
    public void showConstructor() throws Exception {
        Constructor[] constructors = targetClass.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("\t\tparam type : {}", paramType);
            }
        }
    }

    @DisplayName("reflection 테스트 : 필드")
    @Test
    @SuppressWarnings("rawtypes")
    public void field() throws Exception {
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            logger.debug("\t\t{} {}", Modifier.toString(field.getModifiers()), field.getName());
        }
    }

    @DisplayName("reflection 테스트 : 메서드 ")
    @Test
    @SuppressWarnings("rawtypes")
    public void method() throws Exception {
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods) {
            logger.debug("\t\t{} {}", Modifier.toString(method.getModifiers()), method.getName());
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                logger.debug("\t\t\t{} {}", Modifier.toString(parameter.getModifiers()), parameter.getType().getName());
            }
        }
    }

    @DisplayName("reflection 테스트 : private 필드 접근 ")
    @Test
    public void privateFieldAccess() throws Exception {
        final String expectedName = "고유식";
        final int expectedAge = 32;
        Class<Student> clazz = Student.class;
        Student newInstance = clazz.newInstance();

        setField(clazz, "name", expectedName, newInstance);
        setField(clazz, "age", expectedAge, newInstance);

        assertEquals(expectedName, newInstance.getName());
        assertEquals(expectedAge, newInstance.getAge());

    }

    private <T> void setField(Class<T> clazz, String fieldName, Object value, T newInstance) throws NoSuchFieldException, IllegalAccessException {
        Field name = clazz.getDeclaredField(fieldName);
        if (!name.isAccessible()) {
            name.setAccessible(true);
        }
        name.set(newInstance, value);
    }
}
