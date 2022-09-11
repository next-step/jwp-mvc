package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("fields : {}", Arrays.toString(clazz.getDeclaredFields()));
        logger.debug("constructors: {}", Arrays.toString(clazz.getDeclaredConstructors()));
        logger.debug("methods: {}", Arrays.toString(clazz.getDeclaredMethods()));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        Map<Integer, Question> questions = new HashMap();
        for (Constructor constructor : constructors) {
            final int parameterNum = constructor.getParameterTypes().length;
            if (parameterNum == 3) {
                Question question = (Question) constructor.newInstance("지원", "제목1", "내용1");
                questions.put(3, question);
            }
            if (parameterNum == 6) {
                Question question = (Question) constructor.newInstance(1L, "지원", "제목1", "내용1", new Date(0), 0);
                questions.put(6, question);
            }
        }

        final Question expectedQuestionWithThreeParams = new Question("지원", "제목1", "내용1");
        final Question expectedQuestionWithSixParams = new Question(1L, "지원", "제목1", "내용1", new Date(0), 0);
        assertEquals(expectedQuestionWithThreeParams, questions.get(3));
        assertEquals(expectedQuestionWithSixParams, questions.get(6));
    }

    @Test
    public void privateFieldAccess() throws IllegalAccessException, NoSuchFieldException {
        Class<Student> clazz = Student.class;
        final Student student = new Student();

        final Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(student, "지원");
        final Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        ageField.set(student, 25);

        assertEquals("지원", student.getName());
        assertEquals(25, student.getAge());
    }
}
