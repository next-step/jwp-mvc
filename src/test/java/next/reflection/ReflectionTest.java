package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("클래스 정보 출력")
    @Test
    public void showClass() {
        //given
        Class<Question> clazz = Question.class;
        logger.debug("className : {}", clazz.getName());

        //then
        Arrays.asList(clazz.getDeclaredFields())
                .forEach(field -> logger.debug("field : {}", field.getName()));
        Arrays.asList(clazz.getDeclaredConstructors())
                .forEach(constructor -> logger.debug("constructor : {}", constructor.getName()));
        Arrays.asList(clazz.getMethods())
                .forEach(method -> logger.debug("method : {}", method.getName()));
    }

    @DisplayName("인자를 가진 생성자의 인스턴스 생성")
    @ParameterizedTest
    @CsvSource({"3, 0, writer, title, content, 0"
            , "6, 1, writer, title, content, 1, 1565423307000"})
    @SuppressWarnings("rawtypes")
    public void constructor(ArgumentsAccessor argumentsAccessor) throws Exception {
        //given
        Class<Question> clazz = Question.class;

        //when
        Constructor targetConstructor = Stream.of(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterTypes().length == argumentsAccessor.getInteger(0))
                .findFirst().orElseThrow(IllegalAccessError::new);

        Question question = newQuestionInstance(targetConstructor, argumentsAccessor);

        //then
        assertThat(question.getQuestionId()).isEqualTo(argumentsAccessor.getLong(1));
        assertThat(question.getWriter()).isEqualTo(argumentsAccessor.getString(2));
        assertThat(question.getTitle()).isEqualTo(argumentsAccessor.getString(3));
        assertThat(question.getContents()).isEqualTo(argumentsAccessor.getString(4));
        assertThat(question.getCountOfComment()).isEqualTo(argumentsAccessor.getInteger(5));

        if (argumentsAccessor.getInteger(0) == 6) {
            assertThat(question.getCreatedDate().getTime()).isEqualTo(argumentsAccessor.getLong(6));
        }
    }

    private Question newQuestionInstance(Constructor targetConstructor, ArgumentsAccessor argumentsAccessor) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, ParseException {
        if (argumentsAccessor.getInteger(0) == 3) {
            return (Question) targetConstructor.newInstance(argumentsAccessor.getString(2)
                    , argumentsAccessor.getString(3)
                    , argumentsAccessor.getString(4));
        }

        return (Question) targetConstructor.newInstance(argumentsAccessor.getLong(1)
                , argumentsAccessor.getString(2)
                , argumentsAccessor.getString(3)
                , argumentsAccessor.getString(4)
                , new Date(argumentsAccessor.getLong(6))
                , argumentsAccessor.getInteger(5)
        );
    }

    @DisplayName("private field에 값 할당")
    @ParameterizedTest
    @CsvSource({"age,1,name,baby", "age,30,name,man"})
    void privateFieldAccess(ArgumentsAccessor argumentsAccessor) throws Exception {
        //given
        Class<Student> clazz = Student.class;
        Student student = clazz.newInstance();

        //when
        Map<String, Field> fields = Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.toString().startsWith("private"))
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toMap(field -> field.getName(), field -> field));

        fields.get(argumentsAccessor.getString(0)).set(student, argumentsAccessor.getInteger(1));
        fields.get(argumentsAccessor.getString(2)).set(student, argumentsAccessor.getString(3));

        //then
        assertThat(student.getAge()).isEqualTo(argumentsAccessor.getInteger(1));
        assertThat(student.getName()).isEqualTo(argumentsAccessor.getString(3));
    }

    @DisplayName("component scan")
    @Test
    void componentScan() {
        //given
        List<Class<? extends Annotation>> annotationTypes = Arrays
                .asList(Controller.class, Service.class, Repository.class);

        //when
        Reflections reflections = new Reflections("core.di.factory.example");

        //then
        annotationTypes.forEach(annotationType -> {
            reflections.getTypesAnnotatedWith(annotationType)
                    .forEach(annotatedClass ->
                            logger.debug("[{}] : {}", annotationType.getName(), annotatedClass.getName()));
        });
    }
}
