package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            printParameterTypes(parameterTypes);
        }
    }

    private void printParameterTypes(Class[] parameterTypes) {
        logger.debug("paramer length : {}", parameterTypes.length);
        for (Class paramType : parameterTypes) {
            logger.debug("param type : {}", paramType);
        }
    }
    
    @Test
    @SuppressWarnings("rawtypes")
    public void method() throws Exception {
        Class<Question> clazz = Question.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            logger.debug("name: " + method.getName());
            logger.debug("modifier: " + method.getModifiers());
            logger.debug("returntype: " + method.getReturnType());
            printParameterTypes(method.getParameterTypes());

        }
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void fields() throws Exception {
        Class<Question> clazz = Question.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            logger.debug("name: " + field.getName());
            logger.debug("type: " + field.getType());
        }
    }
}
