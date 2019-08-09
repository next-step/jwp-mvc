package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        // constructors
        Constructor[] constructors = clazz.getDeclaredConstructors();
        Arrays.stream(constructors)
                .forEach(constructor -> logger.debug("constructor : {}", constructor));

        // fields
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .forEach(field -> logger.debug("field : {}", field));

        // methods
        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
                .forEach(method -> logger.debug("method : {}", method));
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
    public void constructorWithArguments() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Class<Question> clazz = Question.class;
        Question question = clazz.getConstructor(String.class, String.class, String.class)
                .newInstance("Summer", "Winter", "Spring");

        assertEquals(question.getWriter(), "Summer");
        assertEquals(question.getTitle(), "Winter");
        assertEquals(question.getContents(), "Spring");
    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        Student student = new Student();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "재성");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 40);

        assertEquals("재성", student.getName());
        assertEquals(40, student.getAge());

    }

    @Test
    public void componentScan() {
        Reflections reflections = new Reflections("core.di.factory.example");

        scanBy(reflections, Controller.class);
        scanBy(reflections, Repository.class);
        scanBy(reflections, Service.class);
    }

    private void scanBy(Reflections reflections, Class<? extends Annotation> annotationType) {
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotationType);
        typesAnnotatedWith
                .forEach(type -> logger.debug("type : {}", type.getName()));
    }
}
