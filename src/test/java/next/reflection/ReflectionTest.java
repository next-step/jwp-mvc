package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Question 정보 출력")
    void showClass() {
        Class<Question> clazz = Question.class;
        // 모든 필드
        Field[] fields = clazz.getDeclaredFields();

        // 생성자
        Constructor[] constructors = clazz.getDeclaredConstructors();

        // 메서드
        Method[] methods = clazz.getDeclaredMethods();

        logger.debug(clazz.getName());
        System.out.println("==========================");
        for (Field field : fields) {
            logger.debug(field.toString());
            System.out.println("-----------field----------");
        }

        System.out.println("==========================");
        for (Constructor constructor : constructors) {
            logger.debug(constructor.toString());
            System.out.println("----------constructor-----------");
        }

        System.out.println("==========================");
        for (Method method : methods) {
            logger.debug(method.toString());
            System.out.println("-----------method----------");
        }
    }

    @Test
    @DisplayName("private field 값 할당")
    void privateFieldAccess() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Constructor<Student> constructor = clazz.getConstructor();
        Student student = constructor.newInstance();

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");

        nameField.setAccessible(true);
        ageField.setAccessible(true);

        nameField.set(student, "test");
        ageField.set(student, 4);

        assertAll(
                () -> assertThat(student.getName()).isEqualTo("test"),
                () -> assertThat(student.getAge()).isEqualTo(4)
        );
    }

    @Test
    @DisplayName("인자가 있는 생성자로 인스턴스 생성")
    @SuppressWarnings("rawtypes")
    void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        List<Class> targetParameterTypes = List.of(long.class, String.class, String.class, String.class, Date.class, int.class);

        Constructor targetConstructor = Arrays.stream(constructors)
                .filter(constructor -> targetParameterTypes.equals(List.of(constructor.getParameterTypes())))
                .findFirst()
                .get();

        int questionId = 1;
        String writer = "tester";
        String title = "test";
        String contests = "testContents";
        Date now = Date.from(Instant.now());
        int countOfComment = 0;

        Object question = targetConstructor.newInstance(questionId, writer, title, contests, now, countOfComment);

        assertThat(question).isEqualTo(new Question(questionId, writer, title, contests, now, countOfComment));
    }
}
