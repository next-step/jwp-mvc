package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("클래스 이름");
        logger.debug(clazz.getName());
        logger.debug("필드 이름");
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(i -> logger.debug(i.getName()));
        logger.debug("생성자 이름");
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(i -> logger.debug(i.getName()));
        logger.debug("메소드 이름");
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(i -> logger.debug(i.getName()));
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
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        final Student student = new Student();
        logger.debug(clazz.getName());
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(i -> {
                    i.setAccessible(true);
                    try {
                        if (i.getName().equals("name")) {
                            i.set(student, "재성");
                        }
                        if (i.getName().equals("age")) {
                            i.set(student, 20);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        assertThat(student.getAge()).isEqualTo(20);
        assertThat(student.getName()).isEqualTo("재성");
    }
}
