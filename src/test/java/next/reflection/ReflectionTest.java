package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        // class
        logger.debug("<Class> name={}, modifiers={}", clazz.getName(), clazz.getModifiers());

        // field
        Arrays.stream(clazz.getFields()).forEach(field ->
                logger.debug("<Accessible Field> name={}, modifiers={}", field.getName(), field.getModifiers())
        );
        Arrays.stream(clazz.getDeclaredFields()).forEach(field ->
                logger.debug("<All Field> name={}, modifiers={}", field.getName(), field.getModifiers())
        );

        // constructor
        Arrays.stream(clazz.getConstructors()).forEach(constructor ->
                logger.debug("<Accessible Constructor> name={}, modifiers={}, parameterType={}",
                        constructor.getName(),
                        constructor.getModifiers(),
                        constructor.getParameterTypes()
                )
        );
        Arrays.stream(clazz.getDeclaredConstructors()).forEach(constructor ->
                logger.debug("<All Constructor> name={}, modifiers={}, parameterType={}",
                        constructor.getName(),
                        constructor.getModifiers(),
                        constructor.getParameterTypes())
        );

        // method
        Arrays.stream(clazz.getMethods()).forEach(method ->
                logger.debug("<Accessible Method> name={}, modifiers={}, parameterTypes={}",
                        method.getName(),
                        method.getModifiers(),
                        method.getParameterTypes())
        );
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method ->
                logger.debug("<All Method> name={}, modifiers={}, parameterTypes={}",
                        method.getName(),
                        method.getModifiers(),
                        method.getParameterTypes())
        );
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
