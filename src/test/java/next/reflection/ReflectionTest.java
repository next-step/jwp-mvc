package next.reflection;

import org.assertj.core.internal.bytebuddy.dynamic.ClassFileLocator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;


public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    final int modifiers = field.getModifiers();
                    logger.info("{} {} {}", Modifier.toString(modifiers), field.getType().getSimpleName(), field.getName());
                });
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> {
                    final int modifiers = constructor.getModifiers();
                    final String parameterTypeNames = Arrays.stream(constructor.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(","));
                    logger.info("{} {} {}", Modifier.toString(modifiers), constructor.getName(), parameterTypeNames);
                });
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {
                    final int modifiers = method.getModifiers();
                    final String parameterTypeNames = Arrays.stream(method.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(","));
                    logger.info("{} {} {}", Modifier.toString(modifiers), method.getName(), parameterTypeNames);
                });
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
    void private_field_access() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class<Student> clazz = Student.class;
        Student student = clazz.getConstructor().newInstance();

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");

        nameField.setAccessible(true);
        ageField.setAccessible(true);

        nameField.set(student,"임정택");
        ageField.setInt(student, 30);

        assertThat(nameField.get(student)).isEqualTo("임정택");
        assertThat(ageField.get(student)).isEqualTo(30);
    }
}
