package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void showClass() {
        Class<Question> clazz = Question.class;

        /**
         *  클래스, 모든 필드, 생성자, 메소드에 대한 정보
         */
        logger.debug(clazz.getName());

        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            logger.debug("## field name: {}, field type: {}", field.getName(), field.getType());
        }

        Constructor<?>[] constructors = clazz.getConstructors();

        for (Constructor<?> constructor : constructors) {
            logger.debug("## constructor parameter count: {}", constructor.getParameterCount());
        }

        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            logger.debug("## method name: {}", method.getName());
            logger.debug("## method return type: {}", method.getReturnType());
        }

    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Student student = new Student();
        Class<Student> studentClass = Student.class;

        Field nameField = studentClass.getDeclaredField("name");
        Field ageField = studentClass.getDeclaredField("age");

        nameField.setAccessible(true);
        ageField.setAccessible(true);

        nameField.set(student, "hiro");
        ageField.set(student, 10);

        assertThat(nameField.get(student)).isEqualTo("hiro");
        assertThat(ageField.get(student)).isEqualTo(10);
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
