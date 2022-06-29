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
import java.lang.reflect.Parameter;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("### Field");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            logger.debug("Field - name: {}, type: {}, modifier : {}", declaredField.getName(), declaredField.getType(), declaredField.getModifiers());
        }

        logger.debug("### Constructor");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            logger.debug("Constructor - name : {}, modifier : {}", constructor.getName(), constructor.getModifiers());
            for (Parameter parameter : parameters) {
                logger.debug("Parameter - name : {}, type : {}, modifier : {}", parameter.getName(), parameter.getType(), parameter.getModifiers());
            }
        }

        logger.debug("### Methods");
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            logger.debug("Method - name : {}, return type : {}", declaredMethod.getName(), declaredMethod.getReturnType());
            Parameter[] parameters = declaredMethod.getParameters();
            for (Parameter parameter : parameters) {
                logger.debug("Parameter - name : {}, type : {}, modifier : {}", parameter.getName(), parameter.getType(), parameter.getModifiers());
            }
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
    void privateFieldAccess() throws IllegalAccessException {
        Student student = new Student();

        Field[] declaredFields = Student.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getName().equals("name")) {
                declaredField.setAccessible(true);
                declaredField.set(student, "dean");
            }

            if (declaredField.getName().equals("age")) {
                declaredField.setAccessible(true);
                declaredField.set(student, 30);
            }
        }

        assertThat(student.getAge()).isEqualTo(30);
        assertThat(student.getName()).isEqualTo("dean");
    }

    @Test
    void createQuestion() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        String writer = "저자";
        String title = "제목";
        String contents = "내용";

        Class<Question> questionClass = Question.class;
        Constructor<?>[] constructors = questionClass.getDeclaredConstructors();
        Constructor<?> constructor = constructors[0];
        Question question = (Question) constructor.newInstance(writer, title, contents);

        assertThat(question.getWriter()).isEqualTo(writer);
        assertThat(question.getTitle()).isEqualTo(title);
        assertThat(question.getContents()).isEqualTo(contents);
    }

    @Test
    void componentScan() {
        Reflections reflections = new Reflections("core.di.factory.example");

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllerClasses) {
            logger.debug(controllerClass.getName());
        }

        Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> serviceClass : serviceClasses) {
            logger.debug(serviceClass.getName());
        }

        Set<Class<?>> repositoryClasses = reflections.getTypesAnnotatedWith(Repository.class);
        for (Class<?> repositoryClass : repositoryClasses) {
            logger.debug(repositoryClass.getName());
        }
    }
}
