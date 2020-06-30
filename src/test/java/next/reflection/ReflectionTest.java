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
        logger.debug("--- class ---");
        logger.debug(clazz.getName());

        logger.debug("--- fields ---");
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            logger.debug(field.getType().getName() + " " + field.getName());
        }

        logger.debug("--- constructors ---");
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            logger.debug(constructor.getName() + "(" + getParamInfo(constructor.getParameters()) + ")");
        }

        logger.debug("--- methods ---");
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            logger.debug(method.getReturnType().getName() + " " + method.getName() + "(" + getParamInfo(method.getParameters()) + ")");
        }
    }

    private String getParamInfo(Parameter[] parameters) {
        String paramInfo = "";
        for(int i=0; i<parameters.length; i++) {
            paramInfo += parameters[i].getType().getName();
            if(i < parameters.length - 1) {
                paramInfo += ", ";
            }
        }
        return paramInfo;
    }
}