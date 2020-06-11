package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

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
                    .map(Class::getName)
                    .collect(Collectors.joining(", "));
            logger.debug("Constructor: {} " + className + "({})", modifier, arguments);
        }

        for (Method method : clazz.getDeclaredMethods()) {
            String modifier = Modifier.toString(method.getModifiers());
            String returnType = method.getReturnType().getName();
            String methodName = method.getName();
            String arguments = Arrays.stream(method.getParameterTypes())
                    .map(Class::getName)
                    .collect(Collectors.joining(", "));

            logger.debug("Method: {} {} " + methodName + "({})", modifier, returnType, arguments);
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

    @Test
    @DisplayName("private field에 값을 할당한다.")
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();

        setFieldValue(student, "name", "KingCjy");
        setFieldValue(student, "age", 22);

        logger.debug("Student: {}", student);
        assertThat(student.getName()).isEqualTo("KingCjy");
        assertThat(student.getAge()).isEqualTo(22);
    }

    private void setFieldValue(Object instance, String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    @Test
    public void createInstanceWithParameters() throws Exception {
        Class<?> clazz = Question.class;

        Constructor[] constructors = clazz.getDeclaredConstructors();

        for (Constructor constructor : constructors) {
            Object[] parameters = getParameterInstances(constructor.getParameterTypes());
            constructor.newInstance(parameters);
        }
    }

    private Object[] getParameterInstances(Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object[] instances = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].isPrimitive()) {
                instances[i] = 0;
                continue;
            }

            instances[i] = parameterTypes[i].getConstructor().newInstance();
        }

        return instances;
    }
}
