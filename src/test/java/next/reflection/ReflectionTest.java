package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> logger.debug("field: {}", field.getName()));
        Arrays.stream(clazz.getDeclaredConstructors()).forEach(constructor -> logger.debug("constructor: {}", constructor.getName()));
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> logger.debug("method: {}", method.getName()));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        String expectedWriter = "test";
        String expectedTitle = "test1";
        String expectedContent = "test3";

        Question actual = clazz
                .getConstructor(String.class, String.class, String.class)
                .newInstance(expectedWriter, expectedTitle, expectedContent);

        Assertions.assertThat(actual.getWriter()).isEqualTo(expectedWriter);
        Assertions.assertThat(actual.getTitle()).isEqualTo(expectedTitle);
        Assertions.assertThat(actual.getContents()).isEqualTo(expectedContent);
    }

    @Test
    public void privateFieldAccess() throws Exception {
        String expectedName = "test";
        int expectedAge = 30;

        Class<Student> clazz = Student.class;
        Student student = new Student();

        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(student, expectedName);

        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        ageField.setInt(student, expectedAge);

        Assertions.assertThat(student.getName()).isEqualTo(expectedName);
        Assertions.assertThat(student.getAge()).isEqualTo(expectedAge);
    }

    @Test
    void scan_component() {
        Reflections reflections = new Reflections("core.di.factory.example");
        List<Class> componentAnnotationClasses = Arrays.asList(Controller.class, Service.class, Repository.class);
        Set<Class<?>> classSet = new HashSet<>();

        for (Class componentAnnotationClass : componentAnnotationClasses) {
            classSet.addAll(reflections.getTypesAnnotatedWith(componentAnnotationClass));
        }

        for (Class<?> aClass : classSet) {
            logger.debug(aClass.getName());
        }

    }
}
