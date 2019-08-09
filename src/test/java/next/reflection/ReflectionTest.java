package next.reflection;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.info("fields \n" + StringUtils.join(clazz.getDeclaredFields(), "\n"));
        logger.info("Constructors \n" + StringUtils.join(clazz.getDeclaredConstructors(), "\n"));
        logger.info("Methods \n" + StringUtils.join(clazz.getDeclaredMethods(), "\n"));
    }

    @Test
    void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Student> clazz = Student.class;
        Student student = (Student) clazz.getConstructors()[0].newInstance();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "jun");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 33);

        logger.info(student.toString());
    }

    /**
     * Constructor parameter 의 name 을 가져오는 방법?
     */
    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        final String VALUE = "what is reflection!";
        final String QUALIFIER = "test";
        final Date date = new Date();
        Class<Question> clazz = Question.class;

        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            Object[] arguments = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Qualifier qualifiers = parameters[i].getAnnotation(Qualifier.class);
                if (qualifiers != null) {
                    arguments[i] = qualifiers.value();
                } else if (parameters[i].getType() == String.class) {
                    arguments[i] = VALUE;
                } else if (parameters[i].getType() == long.class) {
                    arguments[i] = 50;
                } else if (parameters[i].getType() == int.class) {
                    arguments[i] = 100;
                } else if (parameters[i].getType() == Date.class) {
                    arguments[i] = date;
                }
            }
            Question question = (Question) constructor.newInstance(arguments);
            assertThat(question.getContents()).isEqualTo(VALUE);
            assertThat(question.getTitle()).isEqualTo(QUALIFIER);
            assertThat(question.getWriter()).isEqualTo(VALUE);
            assertThat(question.getCreatedDate()).isAfterOrEqualsTo(date);
        }
    }

}
