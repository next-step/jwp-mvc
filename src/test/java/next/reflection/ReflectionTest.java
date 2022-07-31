package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        final Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            logger.debug("constructor : {}", constructor.toString());
        }

        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            logger.debug("method : {}", method.toString());
        }

        final Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            logger.debug("field : {}", field.toString());
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

    @DisplayName("Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다")
    @Test
    void privateFieldAccess() throws IllegalAccessException {
        final Class<Student> clazz = Student.class;
        logger.debug("clazz = {}", clazz);

        final Student student = new Student();

        final Field[] declaredFields = clazz.getDeclaredFields();
        for (final Field declaredField : declaredFields) {
            if (declaredField.getName().equals("name")) {
                declaredField.setAccessible(true);
                declaredField.set(student, "홍길동");
            }

            if (declaredField.getName().equals("age")) {
                declaredField.setAccessible(true);
                declaredField.set(student, 20);
            }
        }

        assertThat(student.getName()).isEqualTo("홍길동");
        assertThat(student.getAge()).isSameAs(20);

    }

    @DisplayName("Question 클래스의 인스턴스를 자바 Reflection API를 활용해 생성한다")
    @Test
    void create_question_instance_with_reflection_api() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        final Class<Question> clazz = Question.class;

        final Constructor<?>[] constructors = clazz.getConstructors();
        for (final Constructor<?> constructor : constructors) {
            logger.debug("constructor : {}", constructor.toString());
        }

        final Object actual = constructors[0].newInstance("작성자", "제목", "내용");

        final Question expected = new Question("작성자", "제목", "내용");

        assertThat(actual).isEqualTo(expected);
    }
}
