package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        Constructor<?>[] constructors = clazz.getConstructors();
        Method[] methods = clazz.getMethods();
        Field[] fields = clazz.getDeclaredFields();

        for (Constructor<?> constructor : constructors) {
            logger.debug("constructor : {}", constructor);
        }

        for (Method method : methods) {
            logger.debug("method : {}", method);
        }

        for (Field field : fields) {
            logger.debug("field : {}", field);
        }

    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);

        Student student = new Student();

        name.set(student, "재성");
        age.setInt(student, 10);

        assertThat(student.getName()).isEqualTo("재성");
        assertThat(student.getAge()).isEqualTo(10);
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
    void constructorWithArgs() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<Question> constructor = Question.class.getConstructor(String.class, String.class, String.class);
        Question question = constructor.newInstance("testWriter", "testTitle", "testContents");

        assertThat(question.getWriter()).isEqualTo("testWriter");
        assertThat(question.getTitle()).isEqualTo("testTitle");
        assertThat(question.getContents()).isEqualTo("testContents");
    }
}
