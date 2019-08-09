package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        Arrays.stream(clazz.getDeclaredFields()).forEach(f -> logger.debug(f.getName()));
        Arrays.stream(clazz.getConstructors()).forEach(c -> logger.debug(c.getName()));
        Arrays.stream(clazz.getDeclaredMethods()).forEach(m -> logger.debug(m.getName()));
    }

    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        String name = "영재";
        int age = 31;
        Student student = new Student();

        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(student, name);

        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        ageField.set(student, age);

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getAge()).isEqualTo(age);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            List<Object> parameters = new ArrayList<>();
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
                if (paramType.isPrimitive()) {
                    parameters.add(1);
                    continue;
                }
                parameters.add(paramType.newInstance());
            }
            constructor.newInstance(parameters.toArray());
        }
    }
}
