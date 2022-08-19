package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    private final String ELEMENT_DELIMITER = ",";

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        Field[] fields = clazz.getDeclaredFields();
        Constructor<Question>[] constructors = (Constructor<Question>[]) clazz.getDeclaredConstructors();
        Method[] methods = clazz.getDeclaredMethods();

        logger.debug(String.format("fields : %s", String.join(this.ELEMENT_DELIMITER, this.objectMapToString(fields))));
        logger.debug(String.format("constructors : %s", String.join(this.ELEMENT_DELIMITER, this.objectMapToString(constructors))));
        logger.debug(String.format("methods : %s", String.join(this.ELEMENT_DELIMITER, this.objectMapToString(methods))));
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
    public void privateFieldAccess() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Student> clazz = Student.class;

        Student student = clazz.getConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        Field nameField = this.findField("name", fields);
        nameField.setAccessible(true);
        final String NAME = "fistkim101";
        nameField.set(student, NAME);

        Field ageField = this.findField("age", fields);
        ageField.setAccessible(true);
        final int AGE = 20;
        ageField.set(student, AGE);

        Assertions.assertThat(student.getName()).isEqualTo(NAME);
        Assertions.assertThat(student.getAge()).isEqualTo(AGE);
    }

    @Test
    public void questionConstructorTest() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        Constructor<Question>[] constructors = (Constructor<Question>[]) clazz.getConstructors();
        Constructor<Question> constructor = Arrays.stream(constructors)
                .filter(questionConstructor -> questionConstructor.getParameterTypes().length == 3)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        Question question = constructor.newInstance("writer", "title", "contents");
        Assertions.assertThat(question).isNotNull();
    }

    @Test
    public void componentScanTest() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("core.di.factory.example"))
                .setScanners(Scanners.TypesAnnotated));

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        logger.debug(String.format("controllers : %s", String.join(this.ELEMENT_DELIMITER, this.classMapToString(controllers))));
        logger.debug(String.format("services : %s", String.join(this.ELEMENT_DELIMITER, this.classMapToString(services))));
        logger.debug(String.format("repositories : %s", String.join(this.ELEMENT_DELIMITER, this.classMapToString(repositories))));
    }

    private List<String> classMapToString(Set<Class<?>> classSet) {
        return classSet.stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    private List<String> objectMapToString(Object[] objects) {
        return Arrays.stream(objects)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private Field findField(String fieldName, Field[] fields) {
        return Arrays.stream(fields)
                .filter(field -> field.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
