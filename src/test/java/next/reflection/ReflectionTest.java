package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);


    @DisplayName("Question 클래스 이름 출력")
    @Test
    public void showClassName() {
        Class<Question> clazz = Question.class;
        logger.debug(String.format("ClassName: %s", clazz.getName()));
    }

    @DisplayName("Question 클래스 필드 출력")
    @Test
    public void showFields() {
        Class<Question> clazz = Question.class;
        Field[] fields = clazz.getDeclaredFields();
        logFields(fields);
    }

    private void logFields(Field[] fields) {
        for(Field field : fields) {
            String name = field.getName();
            logger.debug(String.format("FieldName: %s", name));

            logModifier("FieldModifier: %s", field.getModifiers());

            Class<?> type = field.getType();
            logger.debug(String.format("FieldType: %s", type.getName()));
        }
    }

    @DisplayName("Question 클래스 생성자 출력")
    @Test
    public void showConstructors() {
        Class<Question> clazz = Question.class;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        logConstructors(constructors);
    }

    private void logConstructors(Constructor<?>[] constructors) {
        for(Constructor<?> constructor : constructors) {
            String name = constructor.getName();
            logger.debug(String.format("ConstructorName: %s", name));

            logModifier("ConstructorModifier: %s", constructor.getModifiers());

            int paramCount = constructor.getParameterCount();
            logger.debug(String.format("ConstructorParams : %s", paramCount));

            Parameter[] parameters = constructor.getParameters();
            logParameters(parameters);
        }
    }

    @DisplayName("Method 클래스 메서드 출력")
    @Test
    public void showMethods() {
        Class<Question> clazz = Question.class;
        Method[] methods = clazz.getDeclaredMethods();
        logMethods(methods);
    }

    private void logMethods(Method[] methods) {
        for(Method method : methods) {
            String name = method.getName();
            logger.debug(String.format("MethodName: %s", name));

            logModifier("MethodModifier: %s", method.getModifiers());

            Class<?> returnType = method.getReturnType();
            logger.debug(String.format("MethodReturnType: %s", returnType.getTypeName()));

            int paramCount = method.getParameterCount();
            logger.debug(String.format("MethodsParams : %s", paramCount));

            Parameter[] parameters = method.getParameters();
            logParameters(parameters);
        }
    }

    private void logParameters(Parameter[] parameters) {
        for(Parameter parameter : parameters) {
            String name = parameter.getName();
            logger.debug(String.format("ParameterName: %s", name));

            Class<?> type = parameter.getType();
            logger.debug(String.format("ParameterType: %s", type.getName()));
        }
    }

    private void logModifier(String label, int modifier) {
        logger.debug(String.format(label, fromModifier(modifier)));
    }

    private String fromModifier(int modifier) {
        return modifier == 0 ? "" : Modifier.toString(modifier);
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
