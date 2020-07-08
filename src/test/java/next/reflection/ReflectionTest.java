package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        Stream.of(clazz.getDeclaredFields())
                .map(field -> "[Field] Modifiers: " + field.getModifiers()
                        + ", Name: " + field.getName())
                .forEach(logger::info);

        Stream.of(clazz.getDeclaredMethods())
                .map(method -> {
                    String parameterTypes = of(method.getParameterTypes())
                            .map(Class::getTypeName)
                            .collect(joining(","));
                    return "[Method] Modifiers: " + method.getModifiers()
                            + ", Name: " + method.getName()
                            + ", ParameterType: " + parameterTypes;
                }).forEach(logger::info);


        Stream.of(clazz.getDeclaredConstructors())
                .map(constructor -> {
                    String parameterTypes = of(constructor.getParameterTypes())
                            .map(Class::getTypeName)
                            .collect(joining(","));

                    return "[Constructor] Modifiers: " + constructor.getModifiers()
                            + ", Name: " + constructor.getName()
                            + ", ParameterType: " + parameterTypes;
                }).forEach(logger::info);
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
    public void privateFieldAccess() {
        Student student = new Student();
        Field[] declaredFields = student.getClass().getDeclaredFields();
        Stream.of(declaredFields).filter(field -> !field.isAccessible())
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                })
                .forEach(field -> {
                    try {
                        if (field.getName().equals("name")) {
                            field.set(student, "정원");
                        }
                        if (field.getName().equals("age")) {
                            field.set(student, 10);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        logger.debug(student.toString());
        assertThat(student.getName()).isEqualTo("정원");
        assertThat(student.getAge()).isEqualTo(10);
    }

    @Test
    public void questionConstruct() {
        Class<Question> clazz = Question.class;
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        Constructor<?> constructor = of(declaredConstructors)
                .max(comparing(Constructor::getParameterCount))
                .orElseThrow(IllegalArgumentException::new);

        final long questionId = 1L;
        final String writer = "정원";
        final String title = "제목";
        final String contents = "컨첸츠";
        final Date date = new Date();
        final int countOfComment = 3;

        Object[] parameters = new Object[]{questionId, writer, title, contents, date, countOfComment};

        try {
            Question question = (Question) constructor.newInstance(parameters);
            assertThat(question).isEqualTo(new Question(questionId, writer, title, contents, date, countOfComment));

        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
