package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private static final Map<Class<?>, Object> REFERENCE_TYPE_VALUES;

    static {
        REFERENCE_TYPE_VALUES = new HashMap<>();

        REFERENCE_TYPE_VALUES.put(String.class, "TEXT");
        REFERENCE_TYPE_VALUES.put(int.class, 0);
        REFERENCE_TYPE_VALUES.put(long.class, 0L);
        REFERENCE_TYPE_VALUES.put(Date.class, new Date(0L));
    }

    @Test
    @DisplayName("Question 클래스의 필드, 생성자, 메소드 를 모두 출력")
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        // field
        logger.debug("FIELDS================================");
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    logger.debug(field.toString());
                    logger.debug("{} {} {};\n",
                            Modifier.values()[field.getModifiers()],
                            simplify(field.getType()),
                            field.getName());
                });

        // constructor
        logger.debug("Constructor===========================");
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> {
                    logger.debug(constructor.toString());
                    logger.debug("{} {}({})\n",
                            Modifier.values()[constructor.getModifiers()],
                            getClassNameOnly(constructor.getName()),
                            removeSquareBrackets(simplify(constructor.getGenericParameterTypes())));
                });

        // method
        logger.debug("Method================================");
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {
                    logger.debug(method.toString());
                    logger.debug("{} {}({})\n",
                            Modifier.values()[method.getModifiers()],
                            getClassNameOnly(method.getName()),
                            removeSquareBrackets(simplify(method.getGenericParameterTypes())));
                });
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("param length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }

    private static String simplify(final Type type) {
        String typeValue = type.getTypeName();

        return getClassNameOnly(typeValue);
    }

    private static String getClassNameOnly(final String packagedAttachedName) {
        if (!packagedAttachedName.contains(".")) {
            return packagedAttachedName;
        }

        return packagedAttachedName.substring(packagedAttachedName.lastIndexOf('.') + 1);
    }

    private static List<String> simplify(final Type[] types) {
        return Arrays.stream(types)
                .map(ReflectionTest::simplify)
                .collect(Collectors.toList());
    }

    private static String removeSquareBrackets(List<String> origin) {
        return origin.toString()
                .replace("[", "")
                .replace("]", "");
    }

    @Test
    @DisplayName("private 필드에 값 할당하기")
    void injectFieldValue() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);

        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);

        Student student = new Student();
        nameField.set(student, "kwang");
        ageField.set(student, -25);

        assertThat(student.getAge()).isEqualTo(-25);
        assertThat(student.getName()).isEqualTo("kwang");
        logger.debug("{}", student);
    }

    @Test
    @DisplayName("인자가 있는 생성자로 인스턴스 생성하기")
    void createInstanceWithArguments() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Arrays.stream(constructors)
                .map(this::createInstance)
                .forEach(instance -> {
                    Question question = (Question) instance;

                    assertThat(question.getQuestionId()).isEqualTo(REFERENCE_TYPE_VALUES.get(long.class));
                    assertThat(question.getWriter()).isEqualTo(REFERENCE_TYPE_VALUES.get(String.class));
                    assertThat(question.getTitle()).isEqualTo(REFERENCE_TYPE_VALUES.get(String.class));
                    assertThat(question.getContents()).isEqualTo(REFERENCE_TYPE_VALUES.get(String.class));
                    assertThat(question.getCountOfComment()).isEqualTo(REFERENCE_TYPE_VALUES.get(int.class));

                    logger.debug("{}", question);
                });
    }

    private <T> T createInstance(final Constructor<T> constructor) {
        List<Object> arguments = Arrays.stream(constructor.getParameterTypes())
                .map(REFERENCE_TYPE_VALUES::get)
                .collect(Collectors.toList());

        try {
            return constructor.newInstance(arguments.toArray(new Object[arguments.size()]));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.debug("Fail to create instance : {}", e.getMessage());
            return null;
        }
    }
}
