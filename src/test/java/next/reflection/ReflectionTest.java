package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Question 클래스의 모든 필드, 생성자, 메소드 정보를 출력한다.")
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("----> Class Name          : {}", clazz.getName());
        for (Field field : clazz.getDeclaredFields()) {
            logger.debug("----> Class Fields        : {}", field);
        }
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            logger.debug("----> Class Constructors  : {}", constructor);
        }
        for (Method method : clazz.getDeclaredMethods()) {
            logger.debug("----> Class Methods       : {}", method);
        }
    }

    @Test
    @DisplayName("private field 에 값을 할당한다.")
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        Field ageField = clazz.getDeclaredField("age");
        Field nameField = clazz.getDeclaredField("name");
        ageField.setAccessible(true);
        nameField.setAccessible(true);

        Student student = new Student();
        int age = 19;
        String name = "wu2ee";
        ageField.set(student, age);
        nameField.set(student, name);

        assertAll(
                () -> assertThat(student.getAge()).isEqualTo(age),
                () -> assertThat(student.getName()).isEqualTo(name)
        );
    }

    @Test
    @DisplayName("인자를 가진 생성자의 인스턴스를 생성한다.")
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        // given
        Class<Question> clazz = Question.class;
        Constructor<Question> declaredConstructor = clazz.getDeclaredConstructor(long.class, String.class, String.class, String.class, Date.class, int.class);

        long questionId = 1;
        String writer = "wu2ee";
        String title = "만들면서 배우는 Spring";
        String content = "@MVC 프레임워크 구현";
        Date date = Date.from(Instant.now());
        int countOfComment = 3;

        // when
        Question question = declaredConstructor.newInstance(questionId, writer, title, content, date, countOfComment);

        // then
        assertThat(question).isEqualTo(new Question(questionId, writer, title, content, date, countOfComment));
    }
}
