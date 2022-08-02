package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Question 클래스의 필드, 생성자, 메서드 정보 출력")
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            logger.debug("Field : {}", field.toString());
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            logger.debug("Constructor : {}", constructor.toString());
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            logger.debug("Method : {}", method.toString());
        }
    }

    @Test
    @DisplayName("Student 클래스 private field Getter 메서드를 통한 값 출력")
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");

        Student student = new Student();
        setField(nameField, student, "DHLEE");
        setField(ageField, student, 32);

        assertThat(student.getName()).isEqualTo("DHLEE");
        assertThat(student.getAge()).isEqualTo(32);
    }

    public void setField(Field field, Object instance, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }

    @Test
    @DisplayName("Question 클래스 인자를 가진 생성자의 인스턴스 생성")
    public void createInstanceWithArgs() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            logger.debug("Constructor : {}", constructor.toString());
        }

        Date date = new Date();
        Object actual1 = constructors[0].newInstance("DHLEE", "Title", "Contents");
        Object actual2 = constructors[1].newInstance(1, "DHLEE", "Title", "Contents", date, 3);

        assertThat(actual1).isEqualTo(new Question("DHLEE", "Title", "Contents"));
        assertThat(actual2).isEqualTo(new Question(1, "DHLEE", "Title", "Contents", date, 3));
    }
}
