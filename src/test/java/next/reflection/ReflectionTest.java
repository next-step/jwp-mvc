package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("클래스의 정보를 출력하는데 성공한다")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("Question name: {}", clazz.getName());

        Field[] declaredFields = clazz.getDeclaredFields();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        Constructor<?>[] constructors = clazz.getConstructors();

        assertThat(declaredFields)
                .hasSize(6)
                .extracting(Field::getName).contains("questionId", "writer", "title", "contents");

        assertThat(declaredMethods)
                .hasSize(11)
                .extracting(Method::getName).contains("getQuestionId", "update");

        assertThat(constructors).hasSize(2);
    }

    @DisplayName("Student의 접근 불가한 필드에 값 주입에 성공한다")
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
            } else if (field.getName().equals("age")) {
                field.set(student, age);
            }
        }

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getAge()).isEqualTo(age);
    }

    @DisplayName("Question 객체를 생성한다")
    @Test
    void create_constructors() {
        String writer = "juyoung";
        String title = "subject";
        String content = "content";

        Class<Question> clazz = Question.class;
        Question question = ReflectionUtils.newInstance(clazz, writer, title, content);
        logger.debug("Question create constructor : {}", question);

        assertThat(question.getQuestionId()).isNotNull();
        assertThat(question.getWriter()).isEqualTo(writer);
        assertThat(question.getTitle()).isEqualTo(title);
        assertThat(question.getContents()).isEqualTo(content);
    }

    @DisplayName("Question 객체를 생성한다")
    @Test
    void create_constructors2() {
        int questionId = 1;
        String writer = "juyoung";
        String title = "subject";
        String content = "content";
        int countOfComment = 1;

        Class<Question> clazz = Question.class;
        Question question = ReflectionUtils.newInstance(clazz, questionId, writer, title, content, new Date(), countOfComment);

        assertThat(question.getQuestionId()).isEqualTo(questionId);
        assertThat(question.getWriter()).isEqualTo(writer);
        assertThat(question.getTitle()).isEqualTo(title);
        assertThat(question.getContents()).isEqualTo(content);
        assertThat(question.getCountOfComment()).isEqualTo(countOfComment);

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
