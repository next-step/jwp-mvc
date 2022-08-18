package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("name: {}", clazz.getName());

        // 클래스의 모든 필드 출력
        for (Field declaredField : clazz.getDeclaredFields()) {
            logger.debug("declaredField: {}", declaredField.getName());
        }

        // 생성자 정보 출력
        for (Constructor<?> constructor : clazz.getConstructors()) {
            logger.debug("constructor name: {} ", constructor.getName());
            logger.debug("constructor parameter type: {} ", constructor.getParameterTypes());
        }

        // 메소드에 대한 정보를 출력
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            logger.debug("method {}", declaredMethod.getName());
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

    @Test
    void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Student student = new Student();
        Field name = Student.class.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "test");
        Field age = Student.class.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 10);

        assertThat(student.getName()).isEqualTo("test");
        assertThat(student.getAge()).isEqualTo(10);
    }

    @Test
    void createConstructor() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<Question> clazz = Question.class;
        Constructor<Question> declaredConstructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);
        Question question = declaredConstructor.newInstance("tester", "title", "content");

        assertThat(question).isNotNull();
    }

    @Test
    void componentScan() {
        Reflections reflections = new Reflections("core.di.factory.example");

        logger.debug("controller: {}", reflections.getTypesAnnotatedWith(Controller.class));
        logger.debug("service: {}", reflections.getTypesAnnotatedWith(Service.class));
        logger.debug("repository: {}", reflections.getTypesAnnotatedWith(Repository.class));
    }
}
