package next.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("Question 클래스의 필드, 생성자, 메소드에 관한 정보를 logger 를 통해 출력한다.")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("\n\n================================ Field");
        for (Field field : clazz.getDeclaredFields()) {
            logger.debug(field.toString());
        }

        logger.debug("\n\n================================ Constructor");
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            logger.debug(constructor.toString());
        }

        logger.debug("\n\n================================ Method");
        for (Method method : clazz.getDeclaredMethods()) {
            logger.debug(method.toString());
        }
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

    @DisplayName("Reflection API 를 활용해 Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.")
    @Test
    void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");
        nameField.setAccessible(true);
        ageField.setAccessible(true);

        Student student = new Student();
        String name = "고정완";
        int age = 26;
        nameField.set(student, name);
        ageField.set(student, age);

        assertAll(
                () -> assertThat(student.getName()).isEqualTo(name),
                () -> assertThat(student.getAge()).isEqualTo(age)
        );
    }
}
