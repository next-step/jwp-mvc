package next.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Question 테스트")
class QuestionTest {

    @DisplayName("String.class, String.class, String.class 인자를 가진 생성자의 인스턴스를 생성한다.")
    @Test
    void createInstanceOfConstructorWithArgument1() {
        Class<Question> clazz = Question.class;

        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();

        Constructor<?> constructor = Arrays.stream(declaredConstructors)
                .filter(declaredConstructor -> Arrays.equals(declaredConstructor.getParameterTypes(), new Class[]{String.class, String.class, String.class}))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("String.class, String.class, String.class 인자를 가진 생성자가 존재하지 않습니다."));

        try {
            Question question = (Question) constructor.newInstance("홍종완", "자바 reflection", "낯설다");

            assertThat(question).isNotNull();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("long.class, String.class, String.class, String.class, Date.class, int.class 인자를 가진 생성자의 인스턴스를 생성한다.")
    @Test
    void createInstanceOfConstructorWithArgument2() {
        Class<Question> clazz = Question.class;

        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();

        Constructor<?> constructor = Arrays.stream(declaredConstructors)
                .filter(declaredConstructor -> Arrays.equals(declaredConstructor.getParameterTypes(), new Class[]{long.class, String.class, String.class, String.class, Date.class, int.class}))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("long.class, String.class, String.class, String.class, Date.class, int.class 인자를 가진 생성자가 존재하지 않습니다."));

        try {
            Question question = (Question) constructor.newInstance(1L, "홍종완", "자바 reflection", "낯설다", new Date(), 100);

            assertThat(question).isNotNull();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
