package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    @Test
    void create1() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        Constructor constructor = clazz.getDeclaredConstructors()[0];

        String writer = "Yongdae";
        String title = "NEXT STEP 교육 과정";
        String contents = "Reflection을 배우자";

        Question question = (Question) constructor.newInstance(writer, title, contents);

        assertAll(
                () -> assertThat(question.getWriter()).isEqualTo(writer),
                () -> assertThat(question.getTitle()).isEqualTo(title),
                () -> assertThat(question.getContents()).isEqualTo(contents)
        );
    }

    @Test
    void create2() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        Constructor constructor = clazz.getDeclaredConstructors()[1];

        long id = 1;
        String writer = "Yongdae";
        String title = "NEXT STEP 교육 과정";
        String contents = "Reflection을 배우자";
        Date createDate = new Date();
        int countOfComment = 0;

        Question question = (Question) constructor.newInstance(id, writer, title, contents, createDate, countOfComment);

        assertAll(
                () -> assertThat(question.getQuestionId()).isEqualTo(id),
                () -> assertThat(question.getWriter()).isEqualTo(writer),
                () -> assertThat(question.getTitle()).isEqualTo(title),
                () -> assertThat(question.getContents()).isEqualTo(contents),
                () -> assertThat(question.getCreatedDate()).isEqualTo(createDate),
                () -> assertThat(question.getCountOfComment()).isEqualTo(countOfComment)
        );
    }
}
