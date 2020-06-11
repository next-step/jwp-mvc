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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

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
    void createInstanceWithArguments() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Question question = (Question) constructors[0].newInstance("writer", "title", "contents");
        Question question2 = (Question) constructors[1].newInstance(1, "writer2", "title2", "contents2", new Date(), 1);

        assertThat(question.getQuestionId()).isEqualTo(0);
        assertThat(question.getWriter()).isEqualTo("writer");
        assertThat(question.getTitle()).isEqualTo("title");
        assertThat(question.getContents()).isEqualTo("contents");
        assertThat(question.getCountOfComment()).isEqualTo(0);

        assertThat(question2.getQuestionId()).isEqualTo(1);
        assertThat(question2.getWriter()).isEqualTo("writer2");
        assertThat(question2.getTitle()).isEqualTo("title2");
        assertThat(question2.getContents()).isEqualTo("contents2");
        assertThat(question2.getCountOfComment()).isEqualTo(1);

        logger.debug("{}", question);
        logger.debug("{}", question2);
    }
}
