package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        logger.debug("클래스의 모든 필드 정보: \n" + Arrays.toString(clazz.getDeclaredFields())
            .replace(",", "\n")
            .replace("next.reflection.", " ")
            + "\n");
        logger.debug("클래스의 모든 생성자 정보: \n" + Arrays.toString(clazz.getConstructors())
            .replace("),", ")\n")
            .replace("next.reflection.", " ")
            + "\n");
        logger.debug("클래스의 모든 메소드 정보: \n" + Arrays.toString(clazz.getDeclaredMethods())
            .replace("),", ")\n")
            .replace("next.reflection.", " ")
            + "\n");
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

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;

        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);

        Student student = new Student();
        ageField.set(student, 27);
        nameField.set(student, "jason");

        logger.debug(student.toString());
        logger.debug(clazz.getName());
    }
}
