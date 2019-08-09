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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        // 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(it -> logger.debug("Field : {}", it.getName()));
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(it -> logger.debug("constructor : {}", it.getName()));
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(it -> logger.debug("method : {}", it.getName()));
    }

    @Test
    public void privateFieldAccess() throws Exception {
        String name = "이름";
        int age = 5;

        Student student = new Student();
        Class<Student> clazz = Student.class;
        setFieldValue(clazz, student, "name", name);
        setFieldValue(clazz, student, "age", age);

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getAge()).isEqualTo(age);
    }

    private void setFieldValue(Class<Student> clazz, Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            Class[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 3) {
                constructor.newInstance("작성자", "제목", "내용");
            } else if (parameterTypes.length == 6) {
                constructor.newInstance(1, "작성자", "제목", "내용", new Date(), 5);
            }
        }
    }

    @Test
    void di() throws Exception {
        Reflections reflections = new Reflections("core.di.factory.example", new SubTypesScanner(), new TypeAnnotationsScanner());
        findAnnotatedClass(reflections, Controller.class);
        findAnnotatedClass(reflections, Service.class);
        findAnnotatedClass(reflections, Repository.class);
    }

    private void findAnnotatedClass(Reflections reflections, Class<? extends Annotation> annotation) {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(annotation);
        controllers.stream()
                .forEach(it -> logger.debug(it.getName()));
    }
}
