package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @SuppressWarnings("rawtypes")
    void constructor() throws Exception {
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
    void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        assertThat(clazz.getName())
                .as("String getName() : 패키지 + 클래스 이름을 반환한다.")
                .isEqualTo("next.reflection.Question");

        assertThat(clazz.getModifiers())
                .as("int getModifiers() : 클래스의 접근 제어자를 숫자로 반환한다.")
                .isEqualTo(1);

        assertThat(clazz.getFields())
                .as("Field[] getFields() : 접근 가능한 public 필드 목록을 반환한다.")
                .isEmpty();

        List<String> declaredFields = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .collect(toList());
        assertThat(declaredFields)
                .as("Field[] getDeclaredFields() : 모든 필드 목록을 반환한다.")
                .hasSize(6)
                .containsOnly("questionId", "writer", "title", "contents", "createdDate", "countOfComment");

        List<String> constructors = Arrays.stream(clazz.getConstructors())
                .map(Constructor::getName)
                .collect(toList());
        assertThat(constructors)
                .as("Constructor[] getConstructors() : 접근 가능한 public 생성자 목록을 반환한다.")
                .hasSize(2)
                .containsOnly("next.reflection.Question", "next.reflection.Question");

        List<String> declaredConstructors = Arrays.stream(clazz.getDeclaredConstructors())
                .map(Constructor::getName)
                .collect(toList());
        assertThat(declaredConstructors)
                .as("Constructor[] getDeclaredConstructors() : 모든 생성자 목록을 반환한다.")
                .hasSize(2)
                .containsOnly("next.reflection.Question", "next.reflection.Question");

        List<String> methods = Arrays.stream(clazz.getMethods())
                .map(Method::getName)
                .collect(toList());
        assertThat(methods)
                .as("Method[] getMethods() : 부모 클래스, 자신 클래스의 접근 가능한 public 메서드 목록을 반환한다.")
                .hasSize(17)
                .containsOnly(
                        "equals",
                        "toString",
                        "hashCode",
                        "update",
                        "getQuestionId",
                        "getWriter",
                        "getTitle",
                        "getCreatedDate",
                        "getTimeFromCreateDate",
                        "getCountOfComment",
                        "getContents",
                        "wait",
                        "wait",
                        "wait",
                        "getClass",
                        "notify",
                        "notifyAll");

        List<String> declaredMethods = Arrays.stream(clazz.getDeclaredMethods())
                .map(Method::getName)
                .collect(toList());
        assertThat(declaredMethods)
                .as("Method[] getDeclaredMethods() : 상속 제외한 자신의 모든 메서드 목록을 반환한다.")
                .hasSize(11)
                .containsOnly(
                        "equals",
                        "toString",
                        "hashCode",
                        "update",
                        "getTitle",
                        "getQuestionId",
                        "getWriter",
                        "getCreatedDate",
                        "getTimeFromCreateDate",
                        "getCountOfComment",
                        "getContents");
    }

    private enum StudentFields {
        NAME("name"),
        AGE("age")
        ;

        private final String name;

        StudentFields(final String name) {
            this.name = name;
        }

        public static boolean contains(final String name) {
            return Arrays.stream(values())
                    .anyMatch(value -> value.name.equals(name));
        }
    }

    @Test
    void privateFieldAccess() throws IllegalAccessException {
        final Class<Student> clazz = Student.class;

        final Map<StudentFields, Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> StudentFields.contains(field.getName()))
                .peek(field -> field.setAccessible(true))
                .collect(toMap(field -> StudentFields.valueOf(field.getName().toUpperCase()), field -> field));

        final Student student = new Student();
        final Field name = fields.get(StudentFields.NAME);
        name.set(student, "박무진");

        final Field age = fields.get(StudentFields.AGE);
        age.set(student, 45);

        assertThat(student.getName()).isEqualTo("박무진");
        assertThat(student.getAge()).isEqualTo(45);
    }

    @Test
    void createQuestionInstance() {
        final Class<Question> clazz = Question.class;

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("int", 0);
        parameters.put("long", 0L);
        parameters.put("Date", new Date());
        parameters.put("String", "문자열");

        List<Question> questions = Arrays.stream(clazz.getDeclaredConstructors())
                .map(ExceptionWrapper.functionWrapper(constructor -> {
                    Object[] initArgs = Arrays.stream(constructor.getParameterTypes())
                            .map(parameter -> parameters.get(parameter.getSimpleName()))
                            .toArray();

                    return (Question) constructor.newInstance(initArgs);
                }))
                .collect(toList());

        questions.forEach(question -> {
            assertThat(question.getQuestionId()).isEqualTo(0L);
            assertThat(question.getWriter()).isEqualTo("문자열");
            assertThat(question.getTitle()).isEqualTo("문자열");
            assertThat(question.getContents()).isEqualTo("문자열");
            assertThat(question.getCountOfComment()).isEqualTo(0);
        });
    }
}
