package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    public static final String DELIMITER = ",";

    private static final String NAME = "nextstep";
    private static final int AGE = 10;

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
    }

    @DisplayName("Question클래스의 모든 필드, 생성자, 메소드 정보를 출력한다.")
    @Test
    public void printAllByQuestion() {
        Class<Question> questionClass = Question.class;

        String constructors = extractAndJoining(questionClass.getDeclaredConstructors(), DELIMITER);
        String methods = extractAndJoining(questionClass.getDeclaredMethods(), DELIMITER);
        String fields = extractAndJoining(questionClass.getDeclaredFields(), DELIMITER);

        assertThat(constructors).isEqualTo("next.reflection.Question,next.reflection.Question");
        assertThat(methods).isEqualTo("equals,toString,hashCode,update,getContents,getQuestionId,getCreatedDate,getTimeFromCreateDate,getCountOfComment,getWriter,getTitle");
        assertThat(fields).isEqualTo("questionId,writer,title,contents,createdDate,countOfComment");
    }

    private static String extractAndJoining(Member[] objects, String delimiter) {
        return Arrays.stream(objects)
            .map(Member::getName)
            .collect(Collectors.joining(delimiter));
    }

    @DisplayName("Reflection API를 이용해 private field에 값을 할당할 수 있다.")
    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        Constructor<Student> constructor = clazz.getConstructor();

        Student student = constructor.newInstance();

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");

        setField(nameField, student, NAME);
        setField(ageField, student, AGE);

        assertThat(student.getName()).isEqualTo(NAME);
        assertThat(student.getAge()).isEqualTo(AGE);
    }

    private void setField(Field field, Object instance, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }

}
