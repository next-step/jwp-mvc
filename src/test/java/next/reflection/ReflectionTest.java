package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        for (Field field : clazz.getFields()) {
            logger.info("[Question - All Field]: {}", field);
        }

        for (Constructor<?> constructor : clazz.getConstructors()) {
            logger.info("[Question - All Constructor]: {}", constructor);
        }

        for (Method method : clazz.getMethods()) {
            logger.info("[Question - All Method]: {}", method);
        }
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

    @DisplayName("Student class에서 private field에 (name, age) 값 할당")
    @Test
    public void privateFieldAccess() throws Exception {
        Student student = new Student();
        Class<Student> clazz = Student.class;

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "죠르디");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 20);

        assertThat(student.getName()).isEqualTo("죠르디");
        assertThat(student.getAge()).isEqualTo(20);
    }

    @DisplayName("Question 인스턴스 생성")
    @Test
    void createQuestionInstance() throws Exception {
        Class<Question> clazz = Question.class;
        for (Constructor<Question> constructor : (Constructor<Question>[]) clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 3) {
                Question question = constructor.newInstance("writerId", "title", "content");
                logger.debug("Constructor paramCount3: " + question);
            }

            if (constructor.getParameterCount() == 6) {
                Question question = constructor.newInstance(1L, "writerId", "title", "content", Date.from(Instant.now()), 3);
                logger.debug("Constructor paramCount6: " + question);
            }
        }
    }

    @DisplayName("@Controller, @Service, @Repository 애노테이션 설정 클래스 출력")
    @Test
    void componentScanTest() {
        Reflections reflections = new Reflections("core.di.factory.example");

        Set<Class<?>> classes = getClassWithAnnotation(reflections, Controller.class, Service.class, Repository.class);

        for (Class<?> clazz : classes) {
            logger.info("[class name] = {}", clazz.getName());

            logger.info("[class annotation]");
            for (Annotation annotation : clazz.getAnnotations()) {
                logger.info("[annotation name] = {}", annotation);
            }

            logger.info("[class method]");
            for (Method method : clazz.getDeclaredMethods()) {
                logger.info("[method name] = {}", method.getName());
            }
        }

        assertThat(classes).hasSize(4);
    }

    private Set<Class<?>> getClassWithAnnotation(Reflections reflections, Class<? extends Annotation>... classes) {
        return Arrays.stream(classes)
                .flatMap(aClass -> reflections.getTypesAnnotatedWith(aClass)
                        .stream().distinct()
                )
                .collect(Collectors.toSet());
    }
}
