package next.reflection;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private static final String BASIC_PACKAGE = "core.di.factory.example";

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        Field[] fields = clazz.getDeclaredFields();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Method[] methods = clazz.getDeclaredMethods();

        logger.debug(clazz.getName());

        // Field
        Arrays.stream(fields).forEach(
            field -> logger.debug("Field Name: " + Modifier.toString(field.getModifiers()) + " " + field.getName())
        );

        // Constructor
        Arrays.stream(constructors).forEach(
            constructor -> {
                logger.debug("Constructor Name: " + Modifier.toString(constructor.getModifiers()) + " " +  constructor.getName());
                Arrays.stream(constructor.getParameterTypes()).forEach(parameter ->
                    logger.debug("Constructor " + constructor.getName() + "  Parameter Type: "
                    + Modifier.toString(parameter.getModifiers()) + " " +  parameter.getName()));
            }
        );

        // Method
        Arrays.stream(methods).forEach(
            method -> {
                logger.debug("Method Name: " + Modifier.toString(method.getModifiers()) + " " + method.getName());
                Arrays.stream(method.getParameters()).forEach(parameter ->
                    logger.debug("Method " + method.getName() + "  Parameter Type: "
                    + Modifier.toString(parameter.getModifiers()) + " " +  parameter.getName()));
            }

        );

    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            List<Object> parameters = new ArrayList<>();
            logger.debug("parameter length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
                parameters.add(createParameter(paramType));
            }
            constructor.newInstance(parameters.toArray());
        }
    }

    private Object createParameter(Class paramType) throws Exception {
        if(paramType.isPrimitive()) {
            return 1;
        }
        return paramType.newInstance();
    }

    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        Student student = new Student();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);

        name.set(student, "기승");
        age.set(student, 31);

        logger.debug("Student name: " + student.getName());
        logger.debug("Student age: " + student.getAge());
    }

    @Test
    public void componentScan() {
        Set<Class<?>> preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);

        preInstanticateClazz.forEach(component -> {
            logger.debug(component.getName());
        });
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Reflections reflections = new Reflections(BASIC_PACKAGE);
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
