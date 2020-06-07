package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        for (Field field : clazz.getDeclaredFields()) {
            String modifier = Modifier.toString(field.getModifiers());
            String type = field.getType().toString();
            String fieldName = field.getName();

            logger.debug("Field: {} {} {}", modifier, type, fieldName);
        }

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            String modifier = Modifier.toString(constructor.getModifiers());
            String className = constructor.getDeclaringClass().getName();
            String arguments = Arrays.stream(constructor.getParameterTypes())
                    .map(targetParameter -> targetParameter.getName())
                    .collect(Collectors.joining(", "));
            logger.debug("Constructor: {} " + className + "({})", modifier, arguments);
        }

        for (Method method : clazz.getDeclaredMethods()) {
            String modifier = Modifier.toString(method.getModifiers());
            String returnType = method.getReturnType().toString();
            String methodName = method.getName();
            String arguments = Arrays.stream(method.getParameterTypes())
                    .map(targetParameter -> targetParameter.getName())
                    .collect(Collectors.joining(", "));

            logger.debug("Method: {} {} " + methodName + "({})", modifier, returnType, arguments);
        }

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
