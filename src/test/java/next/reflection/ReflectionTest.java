package next.reflection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("class name : {}", clazz.getName());
        line();
        showFields(clazz.getDeclaredFields());
        line();
        showConstructors(clazz.getDeclaredConstructors());
        line();
        showMethods(clazz.getDeclaredMethods());
    }

    private void showFields(Field[] fields) {
        logger.debug("fields count: {}", fields.length);
        for (Field field : fields) {
            logger.debug("type : {}, name: {}", field.getType(), field.getName());
        }
    }

    private void showConstructors(Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {
            logger.debug("constrictor parameter count: {}", constructor.getParameterCount());
            showParameters(constructor.getParameters());
        }
    }

    private void showMethods(Method[] methods) {
        logger.debug("methods count: {}", methods.length);

        for (Method method : methods) {
            logger.debug("returnType: {}, name: {}, parameter count: {}", method.getReturnType(), method.getName(), method.getParameterCount());
            showParameters(method.getParameters());
        }
    }

    private void showParameters(Parameter[] parameters) {
        for (Parameter parameter : parameters) {
            logger.debug("type: {}, name: {}", parameter.getType(), parameter.getName());
        }
    }

    private void line() {
        logger.debug("-----------------------------------------");
    }

    @Test
    public void privateFieldAccess() throws Exception {
        // given
        Student student = new Student();

        // when
        setPrivateFields(student, "재성", 30);

        // then
        Assertions.assertThat(student.getName())
                .isEqualTo("재성");

        Assertions.assertThat(student.getAge())
                .isEqualTo(30);
    }

    private static void setPrivateFields(Student student, String name, int age) throws IllegalAccessException {
        Class<?> clazz = student.getClass();

        for (Field declaredField : clazz.getDeclaredFields()) {
            declaredField.setAccessible(true);

            matchTypeAndSetName(student, name, declaredField);

            matchTypeAndSetAge(student, age, declaredField);
        }
    }

    private static void matchTypeAndSetName(Student student, String name, Field declaredField) throws IllegalAccessException {
        if (declaredField.getType().equals(String.class)) {
            declaredField.set(student, name);
        }
    }

    private static void matchTypeAndSetAge(Student student, int age, Field declaredField) throws IllegalAccessException {
        if (declaredField.getType().equals(int.class)) {
            declaredField.set(student, age);
        }
    }

    @Test
    void createQuestion() throws Exception {
        // given // when
        Question question = createInstance(Question.class, "재성", "질문입니다.", "내용입니다.");

        // then
        Assertions.assertThat(question.getWriter())
                .isEqualTo("재성");
        Assertions.assertThat(question.getTitle())
                .isEqualTo("질문입니다.");
        Assertions.assertThat(question.getContents())
                .isEqualTo("내용입니다.");
    }

    private Question createInstance(Class<Question> clazz, Object... args) throws Exception {
        Constructor<Question> constructor = findConstructor(clazz, args);

        return constructor.newInstance(args);
    }

    private Constructor<Question> findConstructor(Class<Question> clazz, Object[] args) throws Exception {
        Class<?>[] argTypes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);

        return clazz.getDeclaredConstructor(argTypes);
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
