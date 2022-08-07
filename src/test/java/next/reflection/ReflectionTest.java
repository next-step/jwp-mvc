package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug("class name : {}", clazz.getName());
        line();
        showFields(clazz.getDeclaredFields());
        line();
        showConstructors(clazz.getConstructors());
        line();
        showMethods(clazz.getDeclaredMethods());
    }

    private void showFields(Field[] fields) {
        logger.debug("fields count: {}", fields.length);
        for (Field field : fields) {
            logger.debug("type : {}, name: {}", field.getType(), field.getName());
        }
    }

    private void showConstructors(Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {
            logger.debug("constrictor parameter count: {}", constructor.getParameterCount());
            showParameters(constructor.getParameters());
        }
    }

    private void showMethods(Method[] methods) {
        logger.debug("methods count: {}", methods.length);

        for (Method method : methods) {
            logger.debug("returnType: {}, name: {}, parameter count: {}", method.getReturnType(), method.getName(), method.getParameterCount());
            showParameters(method.getParameters());
        }
    }

    private void showParameters(Parameter[] parameters) {
        for (Parameter parameter : parameters) {
            logger.debug("type: {}, name: {}", parameter.getType(), parameter.getName());
        }
    }

    private void line() {
        logger.debug("-----------------------------------------");
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
