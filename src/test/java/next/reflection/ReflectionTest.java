package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("field 정보를 확인한다.")
    @Test
    void showFields() {
        Class<Question> clazz = Question.class;

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            logger.info("field Name:{}, Type:{}", field.getName(), field.getType());
        }

        List<String> names = Arrays.stream(declaredFields)
                .map(Field::getName)
                .collect(Collectors.toList());
        assertThat(names).contains("questionId", "writer", "title", "contents", "createdDate", "countOfComment");
    }

    @DisplayName("constructor 정보를 확인한다.")
    @Test
    void showConstructors() {
        Class<Question> clazz = Question.class;

        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            logger.info("constructor Name:{}, ParameterTypes:{}", constructor.getName(), constructor.getParameterTypes());
        }
        List<String> names = Arrays.stream(constructors)
                .map(Constructor::getName)
                .collect(Collectors.toList());
        assertThat(names).contains("next.reflection.Question");
    }

    @DisplayName("method 정보를 확인한다.")
    @Test
    void showMethods() {
        Class<Question> clazz = Question.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            logger.info("method Name:{}, ParameterTypes:{}, ReturnType:{}", method.getName(), method.getParameterTypes(), method.getReturnType());
        }

        List<String> names = Arrays.stream(declaredMethods)
                .map(Method::getName)
                .collect(Collectors.toList());
        assertThat(names).contains("update", "getQuestionId", "getWriter", "getCreatedDate", "getTimeFromCreateDate",
                "getCountOfComment", "getTitle", "getContents", "equals", "toString", "hashCode");
    }

    @DisplayName("private field에 값을 할당한다.")
    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        Student student = new Student();
        setField(clazz, student, "name", "세희");
        setField(clazz, student, "age", 25);

        assertThat(student.getName()).isEqualTo("세희");
        assertThat(student.getAge()).isEqualTo(25);
    }

    @DisplayName("생성자의 인스턴스를 생성한다")
    @Test
    void newInstance() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Question> clazz = Question.class;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> constructor = constructors[0];
        Question question = (Question) constructor.newInstance("세희", "제목", "내용");

        assertThat(question.getWriter()).isEqualTo("세희");
        assertThat(question.getTitle()).isEqualTo("제목");
        assertThat(question.getContents()).isEqualTo("내용");
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

    private void setField(Class<Student> clazz, Student student, String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(student, value);
    }
}
