package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void findStereoType() {
        Reflections reflections = new Reflections("core.di.factory.example", Scanners.TypesAnnotated);
        Set<Class<?>> controllerAnnotatedClass = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> serviceAnnotatedClass = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositoryAnnotatedClass = reflections.getTypesAnnotatedWith(Repository.class);

        assertThat(controllerAnnotatedClass).hasSize(1);
        assertThat(serviceAnnotatedClass).hasSize(1);
        assertThat(repositoryAnnotatedClass).hasSize(2);

        logger.info("Controller Annotated Class: {}", controllerAnnotatedClass);
        logger.info("Service Annotated Class: {}", serviceAnnotatedClass);
        logger.info("Repository Annotated Class: {}", repositoryAnnotatedClass);

    }

    @DisplayName("Question 클래스는 생성자에 인자를 가지는데, 인자를 가진 생성자의 인스턴스 생성")
    @Test
    void construct() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Question> clazz = Question.class;
        Constructor<?> constructor = clazz.getConstructors()[0];
        Object object = constructor.newInstance("writer1", "title1", "contents1");
        assertThat(object).isInstanceOf(Question.class);
        assertThat((Question) object).isEqualTo(new Question("writer1", "title1", "contents1"));
    }

    @DisplayName("private 필드에 값을 할당하고 getter 메소드를 통해 값이 정상적으로 할당 되었는지 확인한다.")
    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;

        Student student = new Student();
        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "user");
        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 20);

        assertThat(student.getName()).isEqualTo("user");
        assertThat(student.getAge()).isEqualTo(20);

        logger.debug(clazz.getName());
    }

    @DisplayName("reflection을 통해 Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력합니다.")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        // 모든 필드
        Field[] declaredFields = clazz.getDeclaredFields();
        assertThat(declaredFields).hasSize(6);
        Arrays.stream(declaredFields).forEach(field -> logger.info("field: {}", field));

        // 모든 생성자
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        assertThat(constructors).hasSize(2);
        Arrays.stream(constructors).forEach(constructor -> logger.info("constructor: {}", constructor));

        // 모든 메소드
        Method[] declaredMethods = clazz.getDeclaredMethods();
        assertThat(declaredMethods).hasSize(11);
        Arrays.stream(declaredMethods).forEach(method -> logger.info("method: {}", method));

        logger.debug(clazz.getName());
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
