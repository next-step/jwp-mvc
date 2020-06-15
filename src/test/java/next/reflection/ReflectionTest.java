package next.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();
        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "iltaek");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 20);

        logger.debug("student {}'s age is {}", student.getName(), student.getAge());
    }

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("Fields===");
        logFields(clazz);

        logger.debug("Constructors===");
        logConstructors(clazz);

        logger.debug("Methods===");
        logMethods(clazz);
    }

    private void logMethods(Class<Question> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
            .map(method -> method.getName() + "(" + getParameters(method) + ")")
            .forEach(logger::debug);
    }

    private String getParameters(Method method) {
        return Arrays.stream(method.getParameters())
            .map(param -> param.getType().getName() + " " + param.getName())
            .collect(Collectors.joining(", "));
    }

    private void logConstructors(Class<Question> clazz) {
        Arrays.stream(clazz.getDeclaredConstructors())
            .map(con -> con.getName() + "(" + getParameters(con) + ")")
            .forEach(logger::debug);
    }

    private void logFields(Class<Question> clazz) {
        Arrays.stream(clazz.getDeclaredFields())
            .map(field -> field.getType().getName() + " " + field.getName())
            .forEach(logger::debug);
    }

    private String getParameters(Constructor constructor) {
        return Arrays.stream(constructor.getParameters())
            .map(param -> param.getType().getName() + " " + param.getName())
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
}
