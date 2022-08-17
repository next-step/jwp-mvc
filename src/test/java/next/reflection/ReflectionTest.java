package next.reflection;

import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        logger.debug("*** Fields ***");
        Arrays.stream(clazz.getDeclaredFields()).forEach(field ->
                logger.debug("Name: {}, Modifier: {}, Type: {}", field.getName(), Modifier.toString(field.getModifiers()), field.getType())
        );
        logger.debug("*** Constructors ***");
        Arrays.stream(clazz.getDeclaredConstructors()).forEach(constructor ->
                logger.debug("Modifier: {}, Parameters: {}", Modifier.toString(constructor.getModifiers()), constructor.getParameterTypes())
        );
        logger.debug("*** Methods ***");
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method ->
                logger.debug("Name: {}, Return Type: {}, Modifier: {}, Parameters: {}", method.getName(), method.getReturnType(), Modifier.toString(method.getModifiers()), method.getParameterTypes())
        );
        logger.debug(Arrays.toString(clazz.getDeclaredMethods()));
    }

    @Test
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor<?>[] constructors = clazz.getConstructors();

        Question question1 = (Question) constructors[0].newInstance("writer", "title", "contents");
        Question question2 = (Question) constructors[1].newInstance(1L, "writer", "title", "contents", new Date(), 1);

        assertThat(question1).isEqualTo(new Question("writer", "title", "contents"));
        assertThat(question2).isEqualTo(new Question(1L, "writer", "title", "contents", new Date(), 1));
    }

    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        Student student = new Student();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "테스트");
        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 25);

        assertEquals(student.getName(), "테스트");
        assertEquals(student.getAge(), 25);
    }

    private

    @Test
    void componentScan() {
        Reflections reflections = new Reflections("core.di.factory.example");
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);

        logger.debug("Controllers: {}", controllers);
        logger.debug("Services: {}", services);
        logger.debug("Repositories: {}", repositories);
    }
}
