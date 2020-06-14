package next.reflection;

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
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("클래스 정보 출력")
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("class name : {}", clazz.getName());
        logger.debug("class access modifier : {}", Modifier.toString(clazz.getModifiers()));
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            logger.debug("field : {} {}", Modifier.toString(field.getModifiers()), field.getName());
        });
        Arrays.stream(clazz.getDeclaredConstructors()).forEach(constructor -> {
            logger.debug("constructor : {} {}", Modifier.toString(constructor.getModifiers()), constructor.getName());
        });
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
            logger.debug("method : {} {}", Modifier.toString(method.getModifiers()), method.getName());
        });
    }

    @Test
    @DisplayName("private 필드에 값 할당")
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        String name = "eeseul";
        int age = 30;

        Class<Student> clazz = Student.class;

        Student student = clazz.newInstance();
        setField(clazz, "name", name, student);
        setField(clazz, "age", age, student);

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getAge()).isEqualTo(age);
    }

    @Test
    @DisplayName("@Controller, @Service, @Repository 애노테이션 클래스 스캔")
    void componentScan() {
        Reflections reflections = new Reflections("core.di.factory.example");

        Set<Class<?>> classSet = new HashSet<>();
        reflections.getTypesAnnotatedWith(Controller.class).stream()
                .forEach(clazz -> classSet.add(clazz));

        reflections.getTypesAnnotatedWith(Service.class).stream()
                .forEach(clazz -> classSet.add(clazz));

        reflections.getTypesAnnotatedWith(Repository.class).stream()
                .forEach(clazz -> classSet.add(clazz));

        classSet.stream()
                .forEach(clazz -> logger.debug("class : {}", clazz.getSimpleName()));
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

    private void setField(Class<Student> clazz, String fieldName, Object value, Student student) throws NoSuchFieldException, IllegalAccessException {
        Field nameField = clazz.getDeclaredField(fieldName);

        nameField.setAccessible(true);
        nameField.set(student, value);
    }
}
