package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Question class의 필드, 생성자, 메소드 정보 출력")
    public void showClass() {
        Class<Question> clazz = Question.class;
        String fieldsInfo = membersInfo(clazz.getDeclaredFields());
        String constructorInfo = membersInfo(clazz.getConstructors());
        String methodInfo = membersInfo(clazz.getDeclaredMethods());

        logger.debug(clazz.getName());
        logger.debug("fields info : \n{}", fieldsInfo);
        logger.debug("constructor info : \n{}", constructorInfo);
        logger.debug("method info : \n{}", methodInfo);

    }

    private String membersInfo(Member[] members) {
        return Stream.of(members)
                .map(this::makeMemberInfoString)
                .collect(Collectors.joining("\n"));
    }

    private String makeMemberInfoString(Member member) {
        String fieldFormat = "%s %s %s";

        String modifier = Modifier.toString(member.getModifiers());
        String type = getMemberType(member);
        String name = member.getName();
        return String.format(fieldFormat, modifier, type, name);
    }

    private String getMemberType(Member member) {
        if (member instanceof Field) {
            Field field = (Field) member;
            return field.getType().getSimpleName();
        }

        if (member instanceof Method) {
            Method method = (Method) member;
            return method.getReturnType().getSimpleName();
        }

        return "";
    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        String fieldName = "name";
        String testName = "태현";
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Field name = clazz.getDeclaredField(fieldName);
        name.setAccessible(true);
        Student student = new Student();
        name.set(student, testName);

        assertThat(student.getName()).isEqualTo(testName);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getDeclaredConstructors();

        Map<Integer, Object[]> actualValuesMap = getActual();
        List<Question> expectedValues = getExpected();

        for (Constructor constructor : constructors) {
            Object actual = newInstance(actualValuesMap, constructor);
            assertThat(actual).isIn(expectedValues);
        }
    }

    private Object newInstance(Map<Integer, Object[]> actualValuesMap, Constructor constructor) throws Exception {
        int parameterCount = constructor.getParameterCount();
        Object[] actualValues = actualValuesMap.get(parameterCount);

        return constructor.newInstance(actualValues);
    }

    private List<Question> getExpected() {
        Question questionByThreeArgument = new Question("writer", "title", "content");
        Question questionBySixArgument = new Question(1, "writer", "title", "content", new Date(), 2);

        return Lists.newArrayList(questionByThreeArgument, questionBySixArgument);
    }

    private Map<Integer, Object[]> getActual() {
        Object[] threeArguments = Arrays.array("writer", "title", "content");
        Object[] sixArguments = Arrays.array(1, "writer", "title", "content", new Date(), 2);

        Map<Integer, Object[]> actualMap = new HashMap<>();
        actualMap.put(threeArguments.length, threeArguments);
        actualMap.put(sixArguments.length, sixArguments);

        return actualMap;
    }

    @Test
    public void componentScan() {
        Reflections reflections = new Reflections("core.di.factory.example");
        Map<Class<? extends Annotation>, List<String>> componentNamesMap = new HashMap<>();
        componentNamesMap.put(Controller.class, Lists.newArrayList("QnaController"));
        componentNamesMap.put(Service.class, Lists.newArrayList("MyQnaService"));
        componentNamesMap.put(Repository.class, Lists.newArrayList("JdbcQuestionRepository", "JdbcUserRepository"));
        Class<? extends Annotation>[] annotations = Arrays.array(Controller.class, Service.class, Repository.class);

        for (Class<? extends Annotation> annotation : annotations) {
            reflections.getTypesAnnotatedWith(annotation)
                    .forEach(annotationClass -> {
                        List<String> componentNames = componentNamesMap.get(annotation);
                        assertThat(annotationClass.getSimpleName()).isIn(componentNames);
                    });
        }
    }
}
