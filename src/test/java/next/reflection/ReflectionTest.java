package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        for (Field field : clazz.getDeclaredFields()) {
            String modifier = Modifier.toString(field.getModifiers());
            String type = field.getType().toString();
            String fieldName = field.getName();

            logger.debug("Field: {} {} {}", modifier, type, fieldName);
        }

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            String modifier = Modifier.toString(constructor.getModifiers());
            String className = constructor.getDeclaringClass().getName();
            String arguments = Arrays.stream(constructor.getParameterTypes())
                    .map(targetParameter -> targetParameter.getName())
                    .collect(Collectors.joining(", "));
            logger.debug("Constructor: {} " + className + "({})", modifier, arguments);
        }

        for (Method method : clazz.getDeclaredMethods()) {
            String modifier = Modifier.toString(method.getModifiers());
            String returnType = method.getReturnType().toString();
            String methodName = method.getName();
            String arguments = Arrays.stream(method.getParameterTypes())
                    .map(targetParameter -> targetParameter.getName())
                    .collect(Collectors.joining(", "));

            logger.debug("Method: {} {} " + methodName + "({})", modifier, returnType, arguments);
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

    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();

        Field name = clazz.getDeclaredField("name");
        Field age = clazz.getDeclaredField("age");

        name.setAccessible(true);
        age.setAccessible(true);

        name.set(student, "KingCjy");
        age.set(student, 22);

        logger.debug("Student: {}", student);
        assertThat(student.getName()).isEqualTo("KingCjy");
        assertThat(student.getAge()).isEqualTo(22);
    }

    @Test
    public void createInstanceWithParameters() throws Exception {
        Class<?> clazz = Question.class;

        Constructor constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);

        Question question = (Question) constructor.newInstance("writer", "title", "contents");
        logger.debug("Question: {}", question);

        assertThat(question.getWriter()).isEqualTo("writer");
        assertThat(question.getTitle()).isEqualTo("title");
        assertThat(question.getContents()).isEqualTo("contents");
    }
}
