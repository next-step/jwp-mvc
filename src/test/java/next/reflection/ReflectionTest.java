package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("Class Name: {}", clazz.getName());

        logger.debug("Fields");
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    final int modifiers = field.getModifiers();
                    logger.debug("- {} {} {}", Modifier.toString(modifiers), field.getType().getSimpleName(), field.getName());
                });

        logger.debug("Constructors");
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> {
                    final int modifiers = constructor.getModifiers();
                    final String parameterTypeNames = Arrays.stream(constructor.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", "));
                    logger.debug("- {} {}({})", Modifier.toString(modifiers), constructor.getName(), parameterTypeNames);
                });

        logger.debug("Methods");
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {
                    final int modifiers = method.getModifiers();
                    final String parameterTypeNames = Arrays.stream(method.getParameterTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", "));
                    logger.debug("- {} {} {}({})", Modifier.toString(modifiers), method.getReturnType().getSimpleName(), method.getName(), parameterTypeNames);
                });
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() {
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
