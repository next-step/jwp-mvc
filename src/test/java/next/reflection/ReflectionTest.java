package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private Class<Question> clazz = Question.class;

    @Test
    public void showClass() {
        logger.debug(clazz.getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void showConstructor() {
        Constructor[] constructors = clazz.getConstructors();
        Arrays.stream(constructors)
                .forEach(constructor -> logger.debug("parameter count : {}, paramter type : {}",
                        constructor.getParameterCount(), constructor.getParameterTypes()));
    }

    @Test
    void showFields() {
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> logger.debug("filed name : {}, field type : {}", field.getName(), field.getType()));
    }

    @Test
    void showMethod() {
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> logger.debug("method name : {}, parameter count : {}, parameter type : {}, return type : {}",
                        method.getName(), method.getParameterCount(), method.getParameterTypes(), method.getReturnType()));
    }
}
