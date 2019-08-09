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
    private static Class<Question> questionClass;
    private static Class<Student> studentClass;


    @BeforeAll
    public static void init() {
        questionClass = Question.class;
        studentClass = Student.class;
    }

    @DisplayName("reflection 테스트 : 클래스 ")
    @Test
    public void showClass() {
        logger.debug(questionClass.getName());
    }

    @DisplayName("reflection 테스트 : 생성자 ")
    @Test
    @SuppressWarnings("rawtypes")
    public void showConstructor() throws Exception {
        Constructor[] constructors = questionClass.getConstructors();
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
        Field[] fields = questionClass.getDeclaredFields();
        for (Field field : fields) {
            logger.debug("\t\t{} {}", Modifier.toString(field.getModifiers()), field.getName());
        }
    }

    @DisplayName("reflection 테스트 : 메서드 ")
    @Test
    @SuppressWarnings("rawtypes")
    public void method() throws Exception {
        Method[] methods = questionClass.getDeclaredMethods();
        for (Method method : methods) {
            logger.debug("\t\t{} {}", Modifier.toString(method.getModifiers()), method.getName());
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                logger.debug("\t\t\t{} {}", Modifier.toString(parameter.getModifiers()), parameter.getType().getName());
            }
        }
    }

    @DisplayName("reflection 테스트 : private 필드 접근")
    @Test
    public void privateFieldAccess() throws Exception {
        final String expectedName = "고유식";
        final int expectedAge = 32;
        Student newInstance = studentClass.newInstance();

        setField(studentClass, "name", expectedName, newInstance);
        setField(studentClass, "age", expectedAge, newInstance);

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

    @DisplayName("reflection 테스트 : 인자 있는 생성자")
    @Test
    public void newInstanceWithArguments() throws Exception {

        Constructor[] constructors = questionClass.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                args[i] = getTestInstance(parameterTypes[i]);
            }

            Object instance = constructor.newInstance(args);

            logger.debug("생성자 인자 갯수: {}", parameterTypes.length);
            logger.debug("생성자 생성 : {}", instance);
        }
    }

    private Object getTestInstance(Class parameterType) throws Exception {
        if (parameterType.isPrimitive()) {
            return getPrimitiveValue(parameterType);
        }
        return parameterType.newInstance();
    }

    private Object getPrimitiveValue(Class parameterType) {
        if (boolean.class == parameterType) {
            return false;
        } else if (short.class == parameterType) {
            return (short) 1;
        } else if (int.class == parameterType) {
            return 1;
        } else if (long.class == parameterType) {
            return 1L;
        } else if (float.class == parameterType) {
            return 1.1f;
        } else if (double.class == parameterType) {
            return 1.1;
        }
        return 'a';
    }
}
