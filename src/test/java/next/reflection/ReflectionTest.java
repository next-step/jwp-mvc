package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("Question name: {}", clazz.getName());

        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> logger.debug("Field name : {}", field.getName()));

        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(constructor -> logger.debug("Constructor name : {}", constructor.toString()));

        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> logger.debug("Method name : {}", String.valueOf(method)));
    }

    @Test
    public void privateFieldAccess() throws IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        String name = "juyoung";
        int age = 10;

        Student student = new Student();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.getName().equals("name")) {
                field.set(student, name);
            }
            if (field.getName().equals("age")) {
                field.set(student, age);
            }
        }

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getAge()).isEqualTo(age);
    }

    @Test
    void create_constructors() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();

        for (Constructor constructor : declaredConstructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            Question question = newQuestion(parameterTypes.length, constructor);

            logger.debug("Question create constructor : {}", question);
        }
    }

    private Question newQuestion(int countOfParam, Constructor constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (countOfParam > 3) {
            return (Question) constructor.newInstance(1, "juyoung", "subject", "content", new Date(), 1);
        }
        return (Question) constructor.newInstance("juyoung", "subject", "content");
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() {
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
