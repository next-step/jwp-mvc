package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Question 정보 출력")
    void showClass() {
        //given
        Class<Question> questionClass = Question.class;
        //when, then
        assertAll(
                () -> assertThat(questionClass.getDeclaredFields())
                        .extracting(Field::getName)
                        .containsExactlyInAnyOrder("questionId", "writer", "title", "contents", "createdDate", "countOfComment"),
                () -> assertThat(questionClass.getDeclaredConstructors())
                        .extracting(Constructor::getName)
                        .containsExactlyInAnyOrder("next.reflection.Question", "next.reflection.Question"),
                () -> assertThat(questionClass.getDeclaredMethods())
                        .extracting(Method::getName)
                        .containsExactlyInAnyOrder(
                                "getQuestionId", "getWriter", "getTitle", "getContents", "getCreatedDate",
                                "getTimeFromCreateDate", "getCountOfComment", "update", "toString", "hashCode", "equals"
                        )
        );
    }

    @Test
    @DisplayName("private field 값 할당")
    void privateFieldAccess() throws Exception {
        //given
        Student student = new Student();
        int updatedAge = 5;
        String updatedName = "name";

        Class<Student> studentClass = Student.class;
        Field ageField = studentClass.getDeclaredField("age");
        Field nameField = studentClass.getDeclaredField("name");
        //when
        ageField.setAccessible(true);
        nameField.setAccessible(true);
        ageField.set(student, updatedAge);
        nameField.set(student, updatedName);
        //then
        assertAll(
                () -> assertThat(student.getAge()).isEqualTo(updatedAge),
                () -> assertThat(student.getName()).isEqualTo(updatedName)
        );
    }

    @Test
    @DisplayName("인자가 있는 생성자로 인스턴스 생성")
    @SuppressWarnings("rawtypes")
    void constructor() throws Exception {
        //given
        Class<Question> questionClass = Question.class;
        List<Class<?>> targetConstructorTypes = Arrays.asList(Long.TYPE, String.class, String.class, String.class, Date.class, Integer.TYPE);
        Constructor<?> targetConstructor = Stream.of(questionClass.getDeclaredConstructors())
                .filter(constructor -> targetConstructorTypes.equals(Arrays.asList(constructor.getParameterTypes())))
                .findAny()
                .get();

        int id = 10;
        String writer = "writer";
        String title = "title";
        String contents = "contents";
        Date createdAt = Date.from(Instant.now());
        int tenCountOfComment = 5;
        //when
        Object tenIdQuestion = targetConstructor.newInstance(id, writer, title, contents, createdAt, tenCountOfComment);
        //then
        assertThat(tenIdQuestion).isEqualTo(new Question(id, writer, title, contents, createdAt, tenCountOfComment));
    }
}
