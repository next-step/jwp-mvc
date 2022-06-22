package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Arrays.stream(constructors).forEach(it -> logger.debug("constructor Modifier: {}, name: {}, parameters: ({})", Modifier.toString(it.getModifiers()), it.getName(), getParameters(it.getParameters())));

        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods).forEach(it -> logger.debug("method Modifier: {}, name: {}, parameters: ({})", Modifier.toString(it.getModifiers()), it.getName(), getParameters(it.getParameters())));

        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(it -> logger.debug("field Modifier: {}, type: {}, name: {}", Modifier.toString(it.getModifiers()), it.getType().getName(), it.getName()));
    }

    private String getParameters(Parameter[] parameters) {
        return Arrays.stream(parameters)
                .map(it -> {
                    String type = it.getType().getName();
                    String name = it.getName();
                    return type + " " + name;
                })
                .collect(Collectors.joining(", "));
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
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        Student student = clazz.newInstance();

        Field name = clazz.getDeclaredField("name");
        Field age = clazz.getDeclaredField("age");

        name.setAccessible(true);
        age.setAccessible(true);

        name.set(student, "성준");
        age.set(student, 99);

        assertThat(student.getName()).isEqualTo("성준");
        assertThat(student.getAge()).isEqualTo(99);
    }
}
