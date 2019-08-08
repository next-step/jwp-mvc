package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("=========== name: " + clazz.getName());
        logger.debug("=========== fields: " + Arrays.toString(clazz.getDeclaredFields()));
        logger.debug("=========== constructors: " + Arrays.toString(clazz.getDeclaredConstructors()));
        logger.debug("=========== methods: " + Arrays.toString(clazz.getDeclaredMethods()));
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
    public void accessPrivateField() throws Exception {
        Map<String, Object> fieldValues = new HashMap<>();

        fieldValues.put("name", "장소현");
        fieldValues.put("age", 28);

        Class<Student> clazz = Student.class;
        Student student = new Student();
        logger.debug(clazz.getName());
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            field.set(student, fieldValues.get(field.getName()));
        }

        assertThat(student.getName()).isEqualTo("장소현");
        assertThat(student.getAge()).isEqualTo(28);
    }

    @Test
    public void accessConstructorWithArgs() throws Exception {
        Map<Type, Object> parameterValues = new HashMap<>();
        parameterValues.put(Long.TYPE, 100);
        parameterValues.put(Integer.TYPE, 1515);
        parameterValues.put(String.class, "스트링입니다");

        Class<Question> clazz = Question.class;
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            logger.debug(constructor.newInstance(Arrays.stream(constructor.getParameterTypes())
                    .map(parameterValues::get).toArray()).toString());
        }
    }

    @Test
    public void scanAnnotation() {
        Reflections reflections = new Reflections("core.di.factory.example",
                new TypeAnnotationsScanner(), new SubTypesScanner());

        logger.debug(reflections.getTypesAnnotatedWith(Controller.class).toString());
        logger.debug(reflections.getTypesAnnotatedWith(Service.class).toString());
        logger.debug(reflections.getTypesAnnotatedWith(Repository.class).toString());
    }
}