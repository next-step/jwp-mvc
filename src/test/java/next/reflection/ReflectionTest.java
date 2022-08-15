package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import core.di.factory.example.JdbcQuestionRepository;
import core.di.factory.example.JdbcUserRepository;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        Arrays.stream(clazz.getDeclaredFields()).forEach(System.out::println);
        System.out.println();
        Arrays.stream(clazz.getConstructors()).forEach(System.out::println);
        System.out.println();
        Arrays.stream(clazz.getMethods()).forEach(System.out::println);
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
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);

        Student student = new Student();
        name.set(student, "재성");
        age.set(student, 20);

        assertThat(student.getName()).isEqualTo("재성");
        assertThat(student.getAge()).isEqualTo(20);
    }

    @Test
    public void constructorWithParameters() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        Constructor constructor = findConstructorThreeParameters(clazz);

        Question question = (Question) constructor.newInstance("writer", "title", "contents");

        assertThat(question.getWriter()).isEqualTo("writer");
        assertThat(question.getTitle()).isEqualTo("title");
        assertThat(question.getContents()).isEqualTo("contents");
    }

    @Test
    public void componentScan() {
        Reflections reflections = new Reflections("core.di.factory.example");

        Set<Class<?>> set = new HashSet<>();
        set.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        set.addAll(reflections.getTypesAnnotatedWith(Service.class));
        set.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        assertThat(set).contains(
                MyQnaService.class,
                JdbcUserRepository.class,
                QnaController.class,
                JdbcQuestionRepository.class
        );
    }

    private Constructor findConstructorThreeParameters(Class<Question> clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        return Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterTypes().length == 3)
                .findAny()
                .get();
    }

}
