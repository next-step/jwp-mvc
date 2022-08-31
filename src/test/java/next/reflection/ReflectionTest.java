package next.reflection;

import core.annotation.Component;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        logger.debug(Arrays.toString(clazz.getDeclaredFields()));
        logger.debug(Arrays.toString(clazz.getDeclaredConstructors()));
        logger.debug(Arrays.toString(clazz.getDeclaredMethods()));
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
        name.set(student, "yongkwon");
        age.set(student, 32);

        logger.debug("name => " + student.getName());
        logger.debug("age => " + student.getAge());
    }

    @Test
    @DisplayName("요구사항5")
    public void instanceInit() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();

        Question question = (Question)declaredConstructors[0].newInstance("kim", "nextstep", "nextstep is good");

        logger.debug("writer => " + question.getWriter());
        logger.debug("title => " + question.getTitle());
        logger.debug("contents => " + question.getContents());
    }

    @Test
    @DisplayName("요구사항6")
    public void showAnnotations() {
        Reflections reflections = new Reflections("core.di.factory.example");

        Set<Class<?>> classByComponentAnnotation = reflections.getTypesAnnotatedWith(Controller.class);
        classByComponentAnnotation.stream()
                .forEach(System.out::println);

        Set<Class<?>> classByServiceAnnotation = reflections.getTypesAnnotatedWith(Service.class);
        classByServiceAnnotation.stream()
                .forEach(System.out::println);

        Set<Class<?>> classByRepositoryAnnotation = reflections.getTypesAnnotatedWith(Repository.class);
        classByRepositoryAnnotation.stream()
                .forEach(System.out::println);
    }
}
