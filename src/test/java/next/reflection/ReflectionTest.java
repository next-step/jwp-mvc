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
import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("요구사항 1 - 클래스 정보 출력")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        logger.debug(clazz.getName());
        Field[] fields = clazz.getDeclaredFields();
        Constructor[] constructors = clazz.getConstructors();
        Method[] methods = clazz.getDeclaredMethods();

        for(Field name: fields){
            logger.debug("fields : {}", String.valueOf(name));
        }

        for(Constructor name: constructors){
            logger.debug("constructors : {}", String.valueOf(name));
        }

        for(Method name: methods){
            logger.debug("methods : {}", String.valueOf(name));
        }
    }

    @DisplayName("요구사항 4 - private field에 값 할당")
    @Test
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();

        try {
            Field field_name = clazz.getDeclaredField("name");
            Field field_age = clazz.getDeclaredField("age");
            field_name.setAccessible(true);
            field_age.setAccessible(true);

            field_name.set(student, "wonbo");
            field_age.set(student, 10);

            logger.debug("student : {}", student.toString());

            assertThat(student.getName()).isEqualTo("wonbo");
            assertThat(student.getAge()).isEqualTo(10);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("요구사항 5 - 인자를 가진 인스턴스 생성")
    @Test
    void createInstatnce() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<Question> constructor = clazz.getConstructor(String.class, String.class, String.class);
        Question question = constructor.newInstance("writer", "title", "contents");

        assertThat(question).isEqualTo(new Question("writer", "title", "contents"));
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
