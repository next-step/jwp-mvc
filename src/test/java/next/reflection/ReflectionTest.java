package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        printAllFieldInfos(clazz);
        printAllConstructorInfos(clazz);
        printAllMethodInfos(clazz);
    }

    @Test
    @DisplayName("private field 값 할당")
    void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        final String testName = "Test Name";
        final int ageValue = 20;
        Student student = new Student();

        Field name = student.getClass().getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, testName);
        name.setAccessible(false);
        Field age = student.getClass().getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, ageValue);
        age.setAccessible(false);

        assertThat(student.getName()).isEqualTo(testName);
        assertThat(student.getAge()).isEqualTo(ageValue);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        String writer = "writer";
        String title = "title";
        String contents = "contents";

        Constructor<Question> constructor = clazz.getConstructor(String.class, String.class, String.class);
        Question question = constructor.newInstance(writer, title, contents);

        assertThat(question.getWriter()).isEqualTo(writer);
        assertThat(question.getTitle()).isEqualTo(title);
        assertThat(question.getContents()).isEqualTo(contents);
    }

    private void printAllFieldInfos(Class<Question> clazz) {
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> logger.debug("FieldName : {}, FieldModifier : {} ", field.getName(), field.getModifiers()));
    }

    @SuppressWarnings("rawtypes")
    private void printAllConstructorInfos(Class<Question> clazz) {
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            logger.debug("ConstructorName : {}, ConstructorModifier : {}", constructor.getName(), constructor.getModifiers());
            printConstructorParameterInfo(constructor.getParameterTypes());
        }
    }

    @SuppressWarnings("rawtypes")
    private void printConstructorParameterInfo(Class[] parameterTypes) {
        logger.debug("Constructor Parameter length : {}", parameterTypes.length);
        Arrays.stream(parameterTypes)
                .forEach(paramType -> logger.debug("Constructor Param type : {}", paramType));
    }

    private void printAllMethodInfos(Class<Question> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            logger.debug("MethodName : {}, MethodModifier : {}", method.getName(), method.getModifiers());
            Arrays.stream(method.getParameterTypes())
                    .forEach(parameterType -> logger.debug("MethodParameterTypeName: {}", parameterType.getName()));
        }
    }
}
