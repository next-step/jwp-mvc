package next.reflection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

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

    @Test
    void question() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Question> clazz = Question.class;

        Field questionId = accessField("questionId", clazz);
        Field writer = accessField("writer", clazz);
        Field title = accessField("title", clazz);
        Field contents = accessField("contents", clazz);
        Field createdDate = accessField("createdDate", clazz);
        Field countOfComment = accessField("countOfComment", clazz);

        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (final Constructor<?> constructor : constructors) {
            Question question;
            if (constructor.getParameterCount() > 3) {
                question = (Question) constructor.newInstance(1L, "성주", "제목", "내용", new Date(), 0);
                logger.debug("object : {}", question.toString());
            } else {
                question = (Question) constructor.newInstance("성주", "제목", "내용");
            }
            logger.debug("object : {}", question.toString());

            questionId.setLong(question, 100L);
            writer.set(question, "seongju");
            title.set(question, "title");
            contents.set(question, "contents");
            countOfComment.setInt(question, 10);

            logger.debug("change field value {}", question.toString());
        }
    }

    private Field accessField(String name, Class<Question> clazz) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }
}
