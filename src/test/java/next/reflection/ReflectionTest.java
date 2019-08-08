package next.reflection;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Question class의 필드, 생성자, 메소드 정보 출력")
    public void showClass() {
        Class<Question> clazz = Question.class;
        String fieldsInfo = membersInfo(clazz.getDeclaredFields());
        String constructorInfo = membersInfo(clazz.getConstructors());
        String methodInfo = membersInfo(clazz.getDeclaredMethods());

        logger.debug(clazz.getName());
        logger.debug("fields info : \n{}", fieldsInfo);
        logger.debug("constructor info : \n{}", constructorInfo);
        logger.debug("method info : \n{}", methodInfo);

    }

    private String membersInfo(Member[] members) {
        return Stream.of(members)
                .map(this::makeMemberInfoString)
                .collect(Collectors.joining("\n"));
    }

    private String makeMemberInfoString(Member member) {
        String fieldFormat = "%s %s %s";

        String modifier = Modifier.toString(member.getModifiers());
        String type = getMemberType(member);
        String name = member.getName();
        return String.format(fieldFormat, modifier, type, name);
    }

    private String getMemberType(Member member) {
        if (member instanceof Field) {
            Field field = (Field) member;
            return field.getType().getSimpleName();
        }

        if (member instanceof Method) {
            Method method = (Method) member;
            return method.getReturnType().getSimpleName();
        }

        return "";
    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        String fieldName= "name";
        String testName = "태현";
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Field name = clazz.getDeclaredField(fieldName);
        name.setAccessible(true);
        Student student = new Student();
        name.set(student, testName);

        assertThat(student.getName()).isEqualTo(testName);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getDeclaredConstructors();

        Date date = new Date();
        Object[] args = Arrays.array(1, "dave", "title", "content", date, 2);
        Question question =new Question(1, "dave", "title", "content", date, 2);

        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length != 5) break; // date 때문에 5개짜리 생성자만 테스트
            assertThat(constructor.newInstance(args).toString()).isEqualTo(question.toString());
        }
    }
}
