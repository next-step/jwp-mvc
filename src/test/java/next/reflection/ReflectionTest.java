package next.reflection;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug(Arrays.toString(clazz.getDeclaredFields()));
        logger.debug(Arrays.toString(clazz.getConstructors()));
        logger.debug(Arrays.toString(clazz.getMethods()));
    }

    @Test
    void privateFieldAccess() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        var studentClass = Student.class;

        var name = studentClass.getDeclaredField("name");
        name.setAccessible(true);

        var age = studentClass.getDeclaredField("age");
        age.setAccessible(true);

        var student = studentClass.newInstance();
        name.set(student, "hong");
        age.set(student, 20);

        assertThat(student.getName()).isEqualTo("hong");
        assertThat(student.getAge()).isEqualTo(20);
    }

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
