package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        Arrays.stream(constructors).forEach(constructor
                -> printConstructorParameterInfo(constructor.getParameterTypes()));
    }

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        printAllFieldInfos(clazz);
        printAllConstructorInfos(clazz);
        printAllMethodInfos(clazz);
    }

    private void printAllFieldInfos(Class<Question> clazz) {
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> logger.debug("FieldName : {}, FieldModifier : {} ", field.getName(), field.getModifiers()));
    }

    @SuppressWarnings("rawtypes")
    private void printAllConstructorInfos(Class<Question> clazz) {
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            logger.debug("ConstructorName : {}, ConstructorModifier : {}", constructor.getName(), constructor.getModifiers());
            printConstructorParameterInfo(constructor.getParameterTypes());
        }
    }

    @SuppressWarnings("rawtypes")
    private void printConstructorParameterInfo(Class[] parameterTypes) {
        logger.debug("Constructor Parameter length : {}", parameterTypes.length);
        Arrays.stream(parameterTypes)
                .forEach(paramType -> logger.debug("Constructor Param type : {}", paramType));
    }

    private void printAllMethodInfos(Class<Question> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            logger.debug("MethodName : {}, MethodModifier : {}", method.getName(), method.getModifiers());
            Arrays.stream(method.getParameterTypes())
                    .forEach(parameterType -> logger.debug("MethodParameterTypeName: {}", parameterType.getName()));
        }
    }
}
