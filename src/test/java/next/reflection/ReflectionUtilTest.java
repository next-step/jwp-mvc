package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.util.ReflectionUtils.newInstance;
import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtilTest.class);

    @DisplayName("파라미터로 범용적인 객체 생성 방법")
    @Test
    void newInstanceTest() {
        Question question = newInstance(Question.class, "writer", "title", "contents");
        assertThat(question.getWriter()).isEqualTo("writer");
        assertThat(question.getTitle()).isEqualTo("title");
        assertThat(question.getContents()).isEqualTo("contents");

    }

}
