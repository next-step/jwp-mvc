package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);


    @DisplayName("Question 클래스 이름 출력")
    @Test
    public void showClassName() {
        Class<Question> clazz = Question.class;
        logger.debug(String.format("ClassName: %s", clazz.getName()));
    }

    @DisplayName("Question 클래스 필드 출력")
    @Test
    public void showFields() {
        Class<Question> clazz = Question.class;
        Field[] fields = clazz.getDeclaredFields();
        logFields(fields);
    }

    private void logFields(Field[] fields) {
        for(Field field : fields) {
            String name = field.getName();
            logger.debug(String.format("FieldName: %s", name));

            logModifier("FieldModifier: %s", field.getModifiers());

            Class<?> type = field.getType();
            logger.debug(String.format("FieldType: %s", type.getName()));
        }
    }

    @DisplayName("Question 클래스 생성자 출력")
    @Test
    public void showConstructors() {
        Class<Question> clazz = Question.class;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        logConstructors(constructors);
    }

    private void logConstructors(Constructor<?>[] constructors) {
        for(Constructor<?> constructor : constructors) {
            String name = constructor.getName();
            logger.debug(String.format("ConstructorName: %s", name));

            logModifier("ConstructorModifier: %s", constructor.getModifiers());

            int paramCount = constructor.getParameterCount();
            logger.debug(String.format("ConstructorParams : %s", paramCount));

            Parameter[] parameters = constructor.getParameters();
            logParameters(parameters);
        }
    }

    @DisplayName("Method 클래스 메서드 출력")
    @Test
    public void showMethods() {
        Class<Question> clazz = Question.class;
        Method[] methods = clazz.getDeclaredMethods();
        logMethods(methods);
    }

    private void logMethods(Method[] methods) {
        for(Method method : methods) {
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
        for(Parameter parameter : parameters) {
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

    @DisplayName("private 이름과 나이 필드에 값을 할당")
    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        final Student student = clazz.newInstance();

        setPrivateField(student, "name", "진호");
        setPrivateField(student, "age", 36);

        assertThat(student.getName()).isEqualTo("진호");
        assertThat(student.getAge()).isEqualTo(36);
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
        field.setAccessible(false);
    }

    @DisplayName("인자를 가진 Question 클래스의 인스턴스 생성")
    @Test
    public void createQuestion() throws Exception {
        final Class<Question> clazz = Question.class;

        final Constructor<?>[] constructors = clazz.getConstructors();
        for(Constructor<?> constructor : constructors) {
            Question question = createQuestionInstance(constructor);
            logger.debug(question.toString());
        }
    }

    private Question createQuestionInstance(Constructor<?> constructor) throws Exception {
        Object[] threeArgs = new Object[]{"진호", "첫번째 질문", "첫번째 질문 내용"};
        Object[] sixArgs = new Object[]{1, "진호", "두번째 질문", "두번째 질문 내용", new Date(), 0};

        final Class[] parameterTypes = constructor.getParameterTypes();
        if(parameterTypes.length == 3) {
            return (Question)constructor.newInstance(threeArgs);
        }
        return (Question)constructor.newInstance(sixArgs);
    }

    @DisplayName("core.di.factory.example 패키지에서 @Controller, @Service, @Repository 어노테이션 클래스 목록 출력")
    @Test
    public void showAllAnnotatedClassesInPackage() {
        final Set<Class<?>> classes = getTypesAnnotatedWith("core.di.factory.example", Controller.class, Service.class, Repository.class);
        logger.debug(classes.toString());
    }

    private Set<Class<?>> getTypesAnnotatedWith(String packageName, Class<? extends Annotation>... classes) {
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<?>> allTypes = new HashSet<>();

        for(Class<? extends Annotation> clazz : classes) {
            Set<Class<?>> types = reflections.getTypesAnnotatedWith(clazz);
            allTypes.addAll(types);
        }
        return allTypes;
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
