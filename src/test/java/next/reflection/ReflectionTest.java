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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        constructorInfo(clazz);
        fieldInfo(clazz);
        methodInfo(clazz);
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
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        try {
            Student student = clazz.newInstance();
            Field nameField = clazz.getDeclaredField("name");
            Field ageField = clazz.getDeclaredField("age");

            nameField.setAccessible(true);
            ageField.setAccessible(true);

            nameField.set(student, "지선");
            ageField.set(student, 29);

            assertEquals(student.getAge(), 29);
            assertEquals(student.getName(), "지선");
        } catch (InstantiationException e) {
            logger.error("No exist default constructor");
        } catch (IllegalAccessException e) {
            logger.error("not access private field");
        } catch (NoSuchFieldException e) {
            logger.error("Not Found Field");
        }
    }

    private void fieldInfo(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        logger.debug(" Field info =====================");
        for (Field field : fields) {
            String modifier = Modifier.toString(field.getModifiers());
            logger.debug("{} field : {} {} {}", clazz.getName(), modifier, field.getType().toGenericString(), field.getName());
        }
    }

    private void constructorInfo(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();

        logger.debug(" Constructor info =====================");
        for (Constructor constructor : constructors) {
            String modifier = Modifier.toString(constructor.getModifiers());
            String parameterType = Arrays.stream(constructor.getParameterTypes())
                    .map(Class::toGenericString)
                    .collect(Collectors.joining(","));

            logger.debug("Modifier :{} , parameters: {} , Name : {}", modifier, parameterType, constructor.getName());
        }
    }

    private void methodInfo(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        logger.debug(" Method info =====================");
        for (Method method : methods) {
            String parameterType = Arrays.stream(method.getParameterTypes())
                    .map(Class::toGenericString)
                    .collect(Collectors.joining(","));

            logger.debug("Modifier : {} , parameters : {}, Name : {} ", Modifier.toString(method.getModifiers()), parameterType, method.getName());
        }
    }
}
