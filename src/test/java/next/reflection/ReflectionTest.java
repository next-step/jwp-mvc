package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test  // 요구사항1
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        // field
        Field[] fields = clazz.getDeclaredFields();
        logger.debug("filed length : {}", fields.length);
        for (Field field : fields) {
            logger.debug("[field] name : {}, type : {} ", field.getName(), field.getType());
        }

        // constructor
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }

        // method
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            logger.debug("[method] modifier : {}, name : {}, param length : {}",
                    method.getModifiers(), method.getName(), method.getParameterCount());
            Class[] parameterTypes = method.getParameterTypes();
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType.getTypeName());
            }
        }

    }

    @Test  // 요구사항4
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        Student student = clazz.getConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("name")) {
                field.set(student, "동수");
            } else if (field.getName().equals("age")) {
                field.set(student, 30);
            }
        }
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                logger.debug("method name : {}, return data : {}", method.getName(), method.invoke(student));
            }
        }

    }

    @Test  // 요구사항5
    public void makeConstructorWithParameter() throws Exception {
        // 잘 모르겠습니다..
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
