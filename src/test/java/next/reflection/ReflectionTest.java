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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private Class<Question> clazz = Question.class;

    @DisplayName("클래스명 확인")
    @Test
    public void showClass() {

        // when
        String name = clazz.getSimpleName();

        // then
        assertThat(name)
                .isEqualTo("Question");
    }

    @DisplayName("클래스의 생성자 출력")
    @Test
    @SuppressWarnings("rawtypes")
    public void showConstructor() {
        Constructor[] constructors = clazz.getConstructors();
        Arrays.stream(constructors)
                .forEach(constructor -> logger.debug("parameter count : {}, paramter type : {}",
                        constructor.getParameterCount(), constructor.getParameterTypes()));
    }

    @DisplayName("클래스의 필드 이름 출력")
    @Test
    void showFields() {
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> logger.debug("filed name : {}, field type : {}", field.getName(), field.getType()));
    }

    @DisplayName("클래스의 메서드 이름 출력")
    @Test
    void showMethod() {
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> logger.debug("method name : {}, parameter count : {}, parameter type : {}, return type : {}",
                        method.getName(), method.getParameterCount(), method.getParameterTypes(), method.getReturnType()));
    }

    @DisplayName("접근제어자가 private인 필드 값 변경")
    @Test
    void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        Field name = clazz.getDeclaredField("name");
        Field age = clazz.getDeclaredField("age");
        name.setAccessible(true);
        age.setAccessible(true);

        Student student = new Student();
        name.set(student, "dowon");
        age.setInt(student, 27);

        assertThat(student.getName())
                .isEqualTo("dowon");
        assertThat(student.getAge())
                .isEqualTo(27);
    }

    @DisplayName("인자가 있는 생성자를 통해 객체 생성")
    @Test
    void createInstance() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<Question> constructor = clazz.getConstructor(String.class, String.class, String.class);
        Question question = constructor.newInstance("writer", "title", "contents");

        assertThat(question)
                .isEqualTo(new Question("writer", "title", "contents"));
    }

    @DisplayName("애노테이션이 설정된 클래스 이름 출력")
    @Test
    void showClassWithAnnotation() {
        Reflections reflection = new Reflections("core.di.factory.example");
        List<Class<?>> classes = Stream.of(Controller.class, Service.class, Repository.class)
                .map(reflection::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        classes.forEach(clazz -> logger.debug("class name : {}", clazz.getSimpleName()));
    }
}
