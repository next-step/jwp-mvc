package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

@DisplayName("Reflection 테스트")
public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> logger.debug("Class 모든 필드 : {}", field));

        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> logger.debug("Class의 모든 생성자 : {}", constructor));

        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> logger.debug("Class의 모든 메소드 : {}", method));
    }

    @DisplayName("자바 Reflection API를 활용해 Student 클래스의 name과 age 필드에 값을 할당한 후, getter 메소드를 통해 값을 확인한다")
    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;

        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);

        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);

        Student student = new Student();
        nameField.set(student, "홍종완");
        ageField.set(student, 20);

        logger.debug(student.toString());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }
}
