package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionTest {
    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() throws Exception {
        Class<Question> clazz = Question.class;

        log.debug("name: {}", clazz.getName());
        log.debug("modifiers: {}", clazz.getModifiers());

        constructor();
        fields();
        methods();
    }

    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getDeclaredConstructors();

        log.debug("----------------------- constructor starts ------------------------");
        for (Constructor constructor : constructors) {
            log.debug("constructor name: {}", constructor.getName());

            Class[] parameterTypes = constructor.getParameterTypes();
            log.debug("parameter length : {}", parameterTypes.length);

            for (Class paramType : parameterTypes) {
                log.debug("param type : {}", paramType);
            }
            log.debug("");
        }
        log.debug("----------------------- constructor ends ------------------------");
    }

    public void fields() {
        Class<Question> clazz = Question.class;
        log.debug("----------------------- fields starts ------------------------");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            log.debug("name: {}", field.getName());
            log.debug("modifier : {}", field.getModifiers());
            log.debug("");
        }
        log.debug("------------------------- fields end -------------------------");
    }

    private void methods() {
        Class<Question> clazz = Question.class;
        Method[] methods = clazz.getDeclaredMethods();
        log.debug("----------------------- methods starts ------------------------");
        for (Method method : methods) {
            log.debug("name: {}", method.getName());
            log.debug("modifier : {}", method.getModifiers());

            Class[] parameterTypes = method.getParameterTypes();
            log.debug("parameter length : {}", parameterTypes.length);

            for(Class paramType : parameterTypes) {
                log.debug("param type: {}", paramType);
            }
            log.debug("");
        }
        log.debug("----------------------- methods starts ------------------------");
    }
}
