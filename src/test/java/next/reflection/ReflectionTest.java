package next.reflection;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Test
    void 인자를_가진_인스턴스_생성() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        var questionClass = Question.class;
        for (Constructor<?> declaredConstructor : questionClass.getDeclaredConstructors()) {
            if (declaredConstructor.getParameterCount() == 3) {
                Question question = (Question)declaredConstructor.newInstance("hong", "제목", "내용");
                logger.debug("question parameter 3 = {}", question);
            }

            if (declaredConstructor.getParameterCount() == 5) {
                Question question = (Question)declaredConstructor.newInstance(1L, "hong", "제목", "내용", new Date(), 0);
                logger.debug("question parameter 5 = {}", question);
            }
        }
    }

    @Test
    void 애노테이션이_설정된_클래스_출력() {
        var reflections = new Reflections("core.di.factory.example");

        var targetClasses = Stream.of(Repository.class, Controller.class, Service.class)
            .map(reflections::getTypesAnnotatedWith)
            .reduce(new HashSet<>(), Sets::union);

        logger.debug("target classes = {}", targetClasses);
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
