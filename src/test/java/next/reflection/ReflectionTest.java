package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;

import static next.reflection.ReflectionUtils.getTestInstance;
import static next.reflection.ReflectionUtils.setField;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("reflection 테스트")
public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    private static Class<Question> questionClass;
    private static Class<Student> studentClass;


    @BeforeAll
    public static void init() {
        questionClass = Question.class;
        studentClass = Student.class;
    }

    @DisplayName("클래스 출력")
    @Test
    public void showClass() {
        logger.debug(questionClass.getName());
    }

    @DisplayName("생성자 출력")
    @Test
    @SuppressWarnings("rawtypes")
    public void showConstructor() throws Exception {
        Constructor[] constructors = questionClass.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("\t\tparam type : {}", paramType);
            }
        }
    }

    @DisplayName("필드 출력")
    @Test
    @SuppressWarnings("rawtypes")
    public void field() throws Exception {
        Field[] fields = questionClass.getDeclaredFields();
        for (Field field : fields) {
            logger.debug("\t\t{} {}", Modifier.toString(field.getModifiers()), field.getName());
        }
    }

    @DisplayName("메서드 출력")
    @Test
    @SuppressWarnings("rawtypes")
    public void method() throws Exception {
        Method[] methods = questionClass.getDeclaredMethods();
        for (Method method : methods) {
            logger.debug("\t\t{} {}", Modifier.toString(method.getModifiers()), method.getName());
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                logger.debug("\t\t\t{} {}", Modifier.toString(parameter.getModifiers()), parameter.getType().getName());
            }
        }
    }

    @DisplayName("private 필드 접근")
    @Test
    public void privateFieldAccess() throws Exception {
        final String expectedName = "고유식";
        final int expectedAge = 32;
        Student newInstance = studentClass.newInstance();

        setField(studentClass, "name", expectedName, newInstance);
        setField(studentClass, "age", expectedAge, newInstance);

        assertEquals(expectedName, newInstance.getName());
        assertEquals(expectedAge, newInstance.getAge());

    }

    @DisplayName("인자 있는 생성자")
    @Test
    public void newInstanceWithArguments() throws Exception {

        Constructor[] constructors = questionClass.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                args[i] = getTestInstance(parameterTypes[i]);
            }

            Object instance = constructor.newInstance(args);

            logger.debug("생성자 인자 갯수: {}", parameterTypes.length);
            logger.debug("생성자 생성 : {}", instance);
            assertNotNull(instance);
        }
    }

    @DisplayName("컴포넌트 스캔 테스트")
    @Test
    public void componentScan() throws Exception {


        Set<Class<?>> components = scanAnnotations("core.di.factory.example", Controller.class, Service.class, Repository.class);
        for (Class<?> component : components) {
            logger.debug("{}", component);
        }

        assertFalse(components.isEmpty());
    }

    private Set<Class<?>> scanAnnotations(String base, Class<? extends Annotation>... annotations) {
        Reflections reflections = new Reflections(base);
        Set<Class<?>> scannedAnnotations = new HashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            scannedAnnotations.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return scannedAnnotations;
    }
}
