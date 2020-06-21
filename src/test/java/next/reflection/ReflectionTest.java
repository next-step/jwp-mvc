package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void showClass() {
        Class<Question> clazz = Question.class;

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            logger.debug("field : {}", field);
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            logger.debug("constructor : {}", constructor);
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            logger.debug("method : {}", method);
        }
    }

    @Test
    @SuppressWarnings("rawtypes")
    void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<Question> constructor = clazz
            .getDeclaredConstructor(String.class, String.class, String.class);
        Question question = constructor.newInstance("writer", "title", "content");

        assertThat(question.getWriter()).isEqualTo("writer");
        assertThat(question.getTitle()).isEqualTo("title");
        assertThat(question.getContents()).isEqualTo("content");
    }

    @Test
    void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Student student = new Student();
        Class<Student> clazz = Student.class;

        String name = "jjy";
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(student, name);

        int age = 100;
        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);
        ageField.set(student, age);

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getAge()).isEqualTo(age);
    }

    @Test
    void componentScan() {
        Reflections reflections = new Reflections(
            "core.di.factory.example",
            new SubTypesScanner(),
            new TypeAnnotationsScanner()
        );

        reflections.getTypesAnnotatedWith(Controller.class)
            .forEach(clazz -> logger.debug("@Controller : {}", clazz));

        reflections.getTypesAnnotatedWith(Service.class)
            .forEach(clazz -> logger.debug("@Service : {}", clazz));;

            reflections.getTypesAnnotatedWith(Repository.class)
            .forEach(clazz -> logger.debug("@Repository : {}", clazz));;
    }

}
