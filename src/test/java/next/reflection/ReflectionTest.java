package next.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;

        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            logger.debug("field : {}", field);
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for(Constructor constructor : constructors){
            logger.debug("constructor : {}", constructor);
        }

        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            logger.debug("method : {}", method);
        }
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
