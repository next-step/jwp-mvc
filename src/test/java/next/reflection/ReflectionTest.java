package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.JdbcQuestionRepository;
import core.di.factory.example.JdbcUserRepository;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.QnaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private static Stream fieldProvider() {
        return Stream.of(
                Arguments.of("questionId", long.class, Modifier.PRIVATE),
                Arguments.of("writer", String.class, Modifier.PRIVATE),
                Arguments.of("title", String.class, Modifier.PRIVATE),
                Arguments.of("contents", String.class, Modifier.PRIVATE),
                Arguments.of("createdDate", Date.class, Modifier.PRIVATE),
                Arguments.of("countOfComment", int.class, Modifier.PRIVATE)
        );
    }

    private static Stream constructorProvider() {
        return Stream.of(
                Arguments.of("next.reflection.Question",
                        Modifier.PUBLIC,
                        3,
                        new Class<?>[]{String.class, String.class, String.class}),
                Arguments.of("next.reflection.Question",
                        Modifier.PUBLIC,
                        6,
                        new Class<?>[]{long.class, String.class, String.class, String.class, Date.class, int.class})
        );
    }

    private static Stream methodProvider() {
        return Stream.of(
                Arguments.of("getWriter",
                        Modifier.PUBLIC,
                        String.class,
                        0,
                        new Class<?>[]{}),
                Arguments.of("update",
                        Modifier.PUBLIC,
                        void.class,
                        1,
                        new Class<?>[]{Question.class})
        );
    }

    private static Stream questionConstructorParams() {
        return Stream.of(
                Arguments.of(Arrays.asList("진호", "첫번째 질문", "첫번째 질문 내용"), "진호", "첫번째 질문", "첫번째 질문 내용", 0),
                Arguments.of(Arrays.asList(2, "진호", "두번째 질문", "두번째 질문 내용", new Date(), 1), "진호", "두번째 질문", "두번째 질문 내용", 1),
                Arguments.of(Arrays.asList(3L, "진호", "세번째 질문", "세번째 질문 내용", new Date(), 2), "진호", "세번째 질문", "세번째 질문 내용", 2)
        );
    }

    @DisplayName("Question 클래스의 이름 확인")
    @Test
    public void showClassName() {
        Class<Question> clazz = Question.class;
        assertThat(clazz.getName()).isEqualTo("next.reflection.Question");
    }

    @DisplayName("Question 클래스의 모든 필드 확인")
    @ParameterizedTest(name = "name: {0}, type: {1}, modifier: {2}")
    @MethodSource("fieldProvider")
    public void showFields(String name, Class<?> type, int modifier) {
        Class<Question> clazz = Question.class;
        Field[] fields = clazz.getDeclaredFields();
        assertThat(fields).anyMatch(f ->
                f.getName().equals(name)
                        && f.getType().equals(type)
                        && f.getModifiers() == modifier);
    }

    @DisplayName("Question 클래스의 모든 생성자 출력")
    @Test
    public void showConstructors() {
        Class<Question> clazz = Question.class;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        logConstructors(constructors);
        assertThat(constructors).hasSize(2);
    }

    @DisplayName("Question 클래스의 생성자 확인")
    @ParameterizedTest(name = "name: {0}, modifier: {1}, paramCount: {2}, paramTypes: {3}")
    @MethodSource("constructorProvider")
    public void showConstructor(String name, int modifier, int paramCount, Class<?>[] paramTypes) throws NoSuchMethodException {
        Class<Question> clazz = Question.class;
        final Constructor<Question> constructor = clazz.getDeclaredConstructor(paramTypes);
        assertAll("생성자의 이름, 접근제한자, 인수 개수와 타입 검증",
                () -> assertThat(constructor.getName()).isEqualTo(name),
                () -> assertThat(constructor.getModifiers()).isEqualTo(modifier),
                () -> assertThat(constructor.getParameterCount()).isEqualTo(paramCount),
                () -> assertThat(constructor.getParameterTypes()).containsExactly(paramTypes)
        );
    }

    @DisplayName("Question 클래스의 모든 메서드 출력")
    @Test
    public void showMethods() {
        Class<Question> clazz = Question.class;
        Method[] methods = clazz.getDeclaredMethods();
        logMethods(methods);
        assertThat(methods).hasSize(11);
    }

    @DisplayName("Question 클래스의 메서드 확인")
    @ParameterizedTest(name = "name: {0}, modifier: {1}, returnType: {2}, paramCount: {3}, paramTypes: {4}")
    @MethodSource("methodProvider")
    void showMethod(String name, int modifier, Class<?> returnType, int paramCount, Class<?>[] paramTypes) throws NoSuchMethodException {
        Class<Question> clazz = Question.class;
        final Method method = clazz.getMethod(name, paramTypes);
        assertAll("생성자의 이름, 접근제한자, 인수 개수와 타입 검증",
                () -> assertThat(method.getName()).isEqualTo(name),
                () -> assertThat(method.getReturnType()).isEqualTo(returnType),
                () -> assertThat(method.getModifiers()).isEqualTo(modifier),
                () -> assertThat(method.getParameterCount()).isEqualTo(paramCount),
                () -> assertThat(method.getParameterTypes()).isEqualTo(paramTypes)
        );
    }

    @DisplayName("private 이름과 나이 필드에 값을 할당")
    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        final Student student = clazz.newInstance();
        setPrivateField(student, "name", "진호");
        setPrivateField(student, "age", 36);
        assertAll(
                "private 이름과 나이 필드 확인",
                () -> assertThat(student.getName()).isEqualTo("진호"),
                () -> assertThat(student.getAge()).isEqualTo(36)
        );
    }

    @DisplayName("인자를 가진 Question 클래스의 인스턴스 생성")
    @ParameterizedTest()
    @MethodSource("questionConstructorParams")
    public void createQuestion(List<Object> valueList, String name, String title, String contents, int countOfComment) throws Exception {
        final Class<Question> clazz = Question.class;
        Object[] paramValues = valueList.toArray();
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        final Constructor<?> constructor = Arrays.stream(constructors)
                .filter(c -> isAssignable(c.getParameterTypes(), paramValues))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        final Question question = (Question) constructor.newInstance(paramValues);

        assertAll("인자 목록을 이용하여 Question 클래스의 인스턴스를 생성",
                () -> assertThat(question.getWriter()).isEqualTo(name),
                () -> assertThat(question.getTitle()).isEqualTo(title),
                () -> assertThat(question.getContents()).isEqualTo(contents),
                () -> assertThat(question.getCountOfComment()).isEqualTo(countOfComment)
        );
    }

    @DisplayName("core.di.factory.example 패키지에서 @Controller, @Service, @Repository 어노테이션 클래스 목록 출력")
    @Test
    public void showAllAnnotatedClassesInPackage() {
        final Set<Class<?>> classes = getTypesAnnotatedWith("core.di.factory.example", Controller.class, Service.class, Repository.class);
        assertThat(classes).containsExactlyInAnyOrder(MyQnaService.class, JdbcQuestionRepository.class, QnaController.class, JdbcUserRepository.class);
    }

    private void logConstructors(Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {
            String name = constructor.getName();
            logger.debug(String.format("ConstructorName: %s", name));

            logModifier("ConstructorModifier: %s", constructor.getModifiers());

            int paramCount = constructor.getParameterCount();
            logger.debug(String.format("ConstructorParams : %s", paramCount));

            Parameter[] parameters = constructor.getParameters();
            logParameters(parameters);
        }
    }

    private void logMethods(Method[] methods) {
        for (Method method : methods) {
            String name = method.getName();
            logger.debug(String.format("MethodName: %s", name));

            logModifier("MethodModifier: %s", method.getModifiers());

            Class<?> returnType = method.getReturnType();
            logger.debug(String.format("MethodReturnType: %s", returnType.getTypeName()));

            int paramCount = method.getParameterCount();
            logger.debug(String.format("MethodsParams : %s", paramCount));

            Parameter[] parameters = method.getParameters();
            logParameters(parameters);
        }
    }

    private void logParameters(Parameter[] parameters) {
        for (Parameter parameter : parameters) {
            String name = parameter.getName();
            logger.debug(String.format("ParameterName: %s", name));

            Class<?> type = parameter.getType();
            logger.debug(String.format("ParameterType: %s", type.getName()));
        }
    }

    private void logModifier(String label, int modifier) {
        logger.debug(String.format(label, fromModifier(modifier)));
    }

    private String fromModifier(int modifier) {
        return modifier == 0 ? "" : Modifier.toString(modifier);
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
        field.setAccessible(false);
    }

    private Set<Class<?>> getTypesAnnotatedWith(String packageName, Class<? extends Annotation>... classes) {
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<?>> allTypes = new HashSet<>();

        for (Class<? extends Annotation> clazz : classes) {
            Set<Class<?>> types = reflections.getTypesAnnotatedWith(clazz);
            allTypes.addAll(types);
        }
        return allTypes;
    }

    private static boolean isAssignable(Class<?>[] parameterTypes, Object[] values) {
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!AssignableParameterHelper.isAssignable(parameterTypes[i], values[i].getClass())) {
                return false;
            }
        }
        return true;
    }
}
