package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        // class
        logger.debug("<Class> name={}, modifiers={}", clazz.getName(), clazz.getModifiers());

        // field
        Arrays.stream(clazz.getFields()).forEach(field ->
                logger.debug("<Accessible Field> name={}, modifiers={}", field.getName(), field.getModifiers())
        );
        Arrays.stream(clazz.getDeclaredFields()).forEach(field ->
                logger.debug("<All Field> name={}, modifiers={}", field.getName(), field.getModifiers())
        );

        // constructor
        Arrays.stream(clazz.getConstructors()).forEach(constructor ->
                logger.debug("<Accessible Constructor> name={}, modifiers={}, parameterType={}",
                        constructor.getName(),
                        constructor.getModifiers(),
                        constructor.getParameterTypes()
                )
        );
        Arrays.stream(clazz.getDeclaredConstructors()).forEach(constructor ->
                logger.debug("<All Constructor> name={}, modifiers={}, parameterType={}",
                        constructor.getName(),
                        constructor.getModifiers(),
                        constructor.getParameterTypes())
        );

        // method
        Arrays.stream(clazz.getMethods()).forEach(method ->
                logger.debug("<Accessible Method> name={}, modifiers={}, parameterTypes={}",
                        method.getName(),
                        method.getModifiers(),
                        method.getParameterTypes())
        );
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method ->
                logger.debug("<All Method> name={}, modifiers={}, parameterTypes={}",
                        method.getName(),
                        method.getModifiers(),
                        method.getParameterTypes())
        );
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

    @Test
    void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        String inputName = "재성";
        int inputAge = 20;
        try {
            Field name = clazz.getDeclaredField("name");
            Field age = clazz.getDeclaredField("age");

            name.setAccessible(true);
            age.setAccessible(true);

            Student student = new Student();

            name.set(student, inputName);
            age.set(student, inputAge);

            assertThat(student.getName()).isEqualTo(inputName);
            assertThat(student.getAge()).isEqualTo(inputAge);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        logger.debug(clazz.getName());
    }

    @Test
    void createQuestWithArguments() {
        Arrays.stream(Question.class.getConstructors())
                .forEach(constructor -> {
                    try {
                        if (constructor.getParameterCount() == 3) {
                            Object instance = constructor.newInstance(
                                    "writer",
                                    "title",
                                    "contents"
                            );
                            assertThat(instance).isNotNull();
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
