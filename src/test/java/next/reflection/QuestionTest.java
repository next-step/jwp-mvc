package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    @Test
    @DisplayName("인자를 가진 생성자의 인스턴스 생성")
    void newInstance() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Question> clazz = Question.class;

        String writer = "seul";
        String title = "title";
        String contents = "contents";

        Question question = null;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 3) {
                question = (Question) constructor.newInstance(writer, title, contents);

            }
        }

        assertThat(question.getWriter()).isEqualTo(writer);
        assertThat(question.getTitle()).isEqualTo(title);
        assertThat(question.getContents()).isEqualTo(contents);
    }
}
