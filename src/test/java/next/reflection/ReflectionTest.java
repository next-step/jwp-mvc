package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            logger.debug("field name : {}, field type : {}", field.getName(), field.getType());
        }

        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("constructor paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("constructor param type : {}", paramType);
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            logger.debug("method name : {}", method.getName());
            Class[] parameterTypes = method.getParameterTypes();
            logger.debug("method parameter length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("method param type : {}", paramType);
            }
            logger.debug("method return type : {}", method.getReturnType());
        }
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
        Student student = new Student();
        Class<Student> studentClass = Student.class;

        Field nameField = studentClass.getDeclaredField("name");
        Field ageField = studentClass.getDeclaredField("age");

        nameField.setAccessible(true);
        ageField.setAccessible(true);

        nameField.set(student, "thxwelchs");
        ageField.set(student, 1);

        assertThat(nameField.get(student)).isEqualTo("thxwelchs");
        assertThat(ageField.get(student)).isEqualTo(1);
    }

    @Test
    void newInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> questionClass = Question.class;

        Constructor<Question> questionConstructor1 = questionClass.getDeclaredConstructor(String.class, String.class, String.class);
        Constructor<Question> questionConstructor2 = questionClass.getDeclaredConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

        long questionId = 1L;
        String writer = "thxwelchs";
        String title = "this is title";
        String contents = "this is contents";
        Date createdDate = Date.from(LocalDateTime.of(2022, 8, 11, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        int countOfComment = 100;

        Question question1 = questionConstructor1.newInstance(writer, title, contents);
        Question question2 = questionConstructor2.newInstance(questionId, writer, title, contents, createdDate, countOfComment);

        assertAll(
                () -> assertThat(question1.getQuestionId()).isZero(),
                () -> assertThat(question1.getWriter()).isEqualTo(writer),
                () -> assertThat(question1.getTitle()).isEqualTo(title),
                () -> assertThat(question1.getContents()).isEqualTo(contents),
                () -> assertThat(question1.getCountOfComment()).isZero()
        );

        assertAll(
                () -> assertThat(question2.getQuestionId()).isEqualTo(1L),
                () -> assertThat(question2.getWriter()).isEqualTo(writer),
                () -> assertThat(question2.getTitle()).isEqualTo(title),
                () -> assertThat(question2.getContents()).isEqualTo(contents),
                () -> assertThat(question2.getCreatedDate()).isEqualTo(createdDate),
                () -> assertThat(question2.getCountOfComment()).isEqualTo(100)
        );
    }
}
