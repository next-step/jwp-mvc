package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class QuestionTest {

    private final long questionId = 111L;
    private final String writer = "writer!!!";
    private final String title = "title!!!";
    private final String contents = "contents!!!";
    private final Date createdDate = new Date();
    private final int countOfComment = 888;

    @Test
    void newInstance_파라미터_3개() throws Exception {
        Class<Question> questionClass = Question.class;

        Constructor<Question> constructor = questionClass.getConstructor(String.class, String.class, String.class);
        Question question = constructor.newInstance(writer, title, contents);

        assertThat(question.getWriter()).isEqualTo(writer);
        assertThat(question.getTitle()).isEqualTo(title);
        assertThat(question.getContents()).isEqualTo(contents);
    }

    @Test
    void newInstance_파라미터_6개() throws Exception {
        Class<Question> questionClass = Question.class;

        Constructor<Question> constructor = questionClass.getConstructor(
                long.class, String.class, String.class, String.class, Date.class, int.class
        );

        Question question = constructor.newInstance(questionId, writer, title, contents, createdDate, countOfComment);

        assertThat(question.getQuestionId()).isEqualTo(questionId);
        assertThat(question.getWriter()).isEqualTo(writer);
        assertThat(question.getTitle()).isEqualTo(title);
        assertThat(question.getContents()).isEqualTo(contents);
        assertThat(question.getCreatedDate()).isEqualTo(createdDate);
        assertThat(question.getCountOfComment()).isEqualTo(countOfComment);
    }

}