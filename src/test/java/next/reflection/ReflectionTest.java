package next.reflection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private Class<Question> clazz;

    @BeforeEach
    void setUp() {
        clazz = Question.class;
    }

    @Test
    public void showClass() {
        field();
        allConstructor();
        method();
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
    void allConstructor() {
        Constructor[] constructors = clazz.getConstructors();
        for (final Constructor constructor : constructors) {
            logger.debug("constructor : {}", constructor.toString());
        }
    }

    @Test
    void field() {
        Field[] fields = clazz.getDeclaredFields();
        for (final Field declaredField : fields) {
            logger.debug("field : {}", declaredField.toString());
        }
    }

    @Test
    void method() {
        Method[] methods = clazz.getMethods();
        for (final Method method : methods) {
            logger.debug("method : {}", method.toString());
        }
    }

    @Test
    void privateFieldAccess() throws NoSuchFieldException {
        Class<Student> clazz = Student.class;

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");
        nameField.setAccessible(true);
        ageField.setAccessible(true);

        Student student = new Student();
        try {
            nameField.set(student, "성주");
            ageField.set(student, 29);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        logger.debug("object : {}", student.toString());
    }


}
