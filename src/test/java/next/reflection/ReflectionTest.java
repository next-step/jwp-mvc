package next.reflection;

import com.github.jknack.handlebars.internal.lang3.ClassUtils;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("Question all declared fields: [{}]", Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));
        logger.debug("Question declared constructors: [{}]", Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList()));
        logger.debug("Question declared methods: [{}]", Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();
        Field name = getField(clazz, "name");
        name.set(student, "홍종완");

        Field age = getField(clazz, "age");
        age.set(student, 5);

        assertThat(student.getName()).isEqualTo("홍종완");
        assertThat(student.getAge()).isEqualTo(5);
    }

    private Field getField(Class<Student> clazz, String fieldName) throws NoSuchFieldException {
        Field declaredField = clazz.getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        return declaredField;
    }

    @Test
    void createInstanceWithArguments() throws Exception {
        Class<Question> clazz = Question.class;

        Map<String, String> args = new HashMap<>() {
            {
                put("writer", "홍종완");
                put("title", "리플렉션 실습");
                put("contents", "가즈아");
            }
        };

        Question question = instantiate(clazz, args);

        assertThat(question.getWriter()).isEqualTo("홍종완");
        assertThat(question.getTitle()).isEqualTo("리플렉션 실습");
        assertThat(question.getContents()).isEqualTo("가즈아");
    }

    @SuppressWarnings("unchecked")
    private <T> T instantiate(Class<T> clazz, Map<String, ?> args) throws Exception {
        final Constructor<T> targetConstructor = (Constructor<T>) Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterCount() == args.size())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("적절한 생성자가 없습니다."));

        final T instance = targetConstructor.newInstance(new Object[targetConstructor.getParameterTypes().length]);

        for (Map.Entry<String, ?> arg : args.entrySet()) {
            Field field = clazz.getDeclaredField(arg.getKey());
            field.setAccessible(true);
            field.set(instance, arg.getValue());
        }

        return instance;
    }

    @Test
    public void componentScan() {
        Set<Class<?>> typesAnnotatedWith = getTypesAnnotatedWith(List.of(Controller.class, Service.class, Repository.class));
        logger.debug("beans: [{}]", typesAnnotatedWith);
    }

    private Set<Class<?>> getTypesAnnotatedWith(List<Class<? extends Annotation>> annotations) {
        Reflections reflections = new Reflections("core.di.factory.example");

        Set<Class<?>> beans = new HashSet<>();
        annotations.forEach(annotation -> beans.addAll(reflections.getTypesAnnotatedWith(annotation)));

        return beans;
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

}
