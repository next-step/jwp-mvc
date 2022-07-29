package next.reflection;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("요구사항 1 - Question 클래스의 모든 필드, 생성자, 메서드에 대한 정보를 출력한다.")
    @Test
    void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        printAllFields(clazz);
        printAllConstructors(clazz);
        printAllMethods(clazz);

        assertThat(clazz.getDeclaredFields()).hasSize(6);
        assertThat(clazz.getDeclaredConstructors()).hasSize(2);
        assertThat(clazz.getDeclaredMethods()).hasSize(11);
    }

    private void printAllFields(Class<Question> clazz) {
        logger.debug("======== 필드 정보 출력 ========");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            logger.debug("{} {}", field.getType().getSimpleName(), field.getName());
        }
        logger.debug("=============================");
    }

    private void printAllConstructors(Class<Question> clazz) {
        logger.debug("======== 생성자 정보 출력 ========");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            String name = constructor.getName();
            String parameterTypes = Arrays.stream(constructor.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
            logger.debug("{}({})", name, parameterTypes);
        }
        logger.debug("=============================");
    }

    private void printAllMethods(Class<Question> clazz) {
        logger.debug("======== 메서드 정보 출력 ========");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            String returnType = method.getReturnType().getSimpleName();
            String parameterClasses = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
            logger.debug("{} {}({})", returnType, name, parameterClasses);
        }
        logger.debug("=============================");
    }

    @DisplayName("요구사항 4 - Student 클래스의 name과 age 필드에 값을 할당한다.")
    @Test
    void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        Field name = clazz.getDeclaredField("name");
        Field age = clazz.getDeclaredField("age");

        Student student = new Student();
        name.setAccessible(true);
        name.set(student, "홍길동");
        age.setAccessible(true);
        age.set(student, 30);

        assertThat(student.getName()).isEqualTo("홍길동");
        assertThat(student.getAge()).isEqualTo(30);
    }

    @DisplayName("요구사항 5 - Question 클래스의 인스턴스를 생성한다.")
    @Test
    void newInstance() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<Question> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);
        Question question = constructor.newInstance("홍길동", "제목입니다", "내용입니다");

        assertThat(question.getWriter()).isEqualTo("홍길동");
        assertThat(question.getTitle()).isEqualTo("제목입니다");
        assertThat(question.getContents()).isEqualTo("내용입니다");
    }

    @Test
    @SuppressWarnings("rawtypes")
    void constructor() {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("parameter length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("parameter type : {}", paramType);
            }
        }
    }
}
