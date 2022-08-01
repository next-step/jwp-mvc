package next.reflection;

import org.junit.jupiter.api.DisplayName;
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
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : declaredConstructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }

        logger.debug("-----------------------------------------------------------");

        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }

    @Test
    public void field() {
        Class<Question> clazz = Question.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            logger.debug("Modifier : {}, Tyep : {}, name : {}, ", field.getModifiers(), field.getType(), field.getName());
        }

        logger.debug("-----------------------------------------------------------");

        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            logger.debug("Modifier : {}, Tyep : {}, name : {}, ", field.getModifiers(), field.getType(), field.getName());
        }
    }

    @Test
    public void method() {
        Class<Question> clazz = Question.class;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            logger.debug("Modifier : {}, ReturnType : {},  Name : {}", method.getModifiers(), method.getReturnType(), method.getName());
            logger.debug("param length : {}, param Count : {}", parameterTypes.length, method.getParameterCount());
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
            logger.debug("------------------------");
        }

        logger.debug("-----------------------------------------------------------");

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            logger.debug("Modifier : {}, ReturnType : {},  Name : {}", method.getModifiers(), method.getReturnType(), method.getName());
            logger.debug("param length : {}, param Count : {}", parameterTypes.length, method.getParameterCount());
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
            logger.debug("------------------------");
        }
    }

    @DisplayName("Private필드에접근하기")
    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        Student student = new Student();
        String name = "형주";
        int age = 36;

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");
        nameField.setAccessible(true);
        ageField.setAccessible(true);

        nameField.set(student, name);
        ageField.set(student, age);

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getAge()).isEqualTo(age);
    }
}
