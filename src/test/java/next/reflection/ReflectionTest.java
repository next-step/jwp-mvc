package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("클래스 정보 출력")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("Class Name: {}", clazz.getName());

        logger.debug("Fields");
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    final int modifiers = field.getModifiers();
                    logger.debug("- {} {} {}", Modifier.toString(modifiers), field.getType().getSimpleName(), field.getName());
                });

        logger.debug("Constructors");
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> {
                    final int modifiers = constructor.getModifiers();
                    final String parameterTypeNames = Arrays.stream(constructor.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", "));
                    logger.debug("- {} {}({})", Modifier.toString(modifiers), constructor.getName(), parameterTypeNames);
                });

        logger.debug("Methods");
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {
                    final int modifiers = method.getModifiers();
                    final String parameterTypeNames = Arrays.stream(method.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", "));
                    logger.debug("- {} {} {}({})", Modifier.toString(modifiers), method.getReturnType().getSimpleName(), method.getName(), parameterTypeNames);
                });
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() {
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

    @DisplayName("private field에 값 할당")
    @Test
    public void privateFieldAccess() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        final Constructor<Student> constructor = clazz.getConstructor();
        final Student student = constructor.newInstance();

        final Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(student, "학생");

        final Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        ageField.setInt(student, 14);

        final Method getName = clazz.getDeclaredMethod("getName");
        final String name = (String) getName.invoke(student);

        final Method getAge = clazz.getDeclaredMethod("getAge");
        final int age = (int) getAge.invoke(student);

        assertThat(name).isEqualTo("학생");
        assertThat(age).isEqualTo(14);
    }

    @DisplayName("인자를 가진 생성자의 인스턴스 생성")
    @Test
    void constructorWithParameters() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        final Constructor<Question> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);
        final Question question = constructor.newInstance("작성자", "제목", "내용");

        assertThat(question).isNotNull();
    }
}
