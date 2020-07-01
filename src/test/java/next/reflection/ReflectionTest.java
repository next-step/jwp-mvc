package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Date;

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

    @Test
    public void privateFieldAccess() {
        try {
            Class<Student> clazz = Student.class;

            Field name = clazz.getDeclaredField("name");
            Field age = clazz.getDeclaredField("age");
            name.setAccessible(true);
            age.setAccessible(true);

            Student student = clazz.newInstance();
            name.set(student, "형근");
            age.setInt(student, 29);

            logger.debug(clazz.getName());
            logger.debug("name : {}, age : {}", student.getName(), student.getAge());
        } catch (Exception e) {
            logger.error("error", e);
        }
    }

    @Test
    public void callConstructorWithParameter() {
        Class<Question> clazz = Question.class;
        long questionId = 100L;
        String writer = "형근";
        String title = "스프링";
        String contents = "잘 알고 싶다.";
        Date createdDate = new Date();
        int countOfComment = 2;

        try {
            Constructor[] constructors = clazz.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                Parameter[] parameters = constructor.getParameters();
                if (parameters.length == 3) {
                    logger.debug("Question {}", constructor.newInstance(writer, title, contents));
                } else if (parameters.length == 6) {
                    logger.debug("Question {}", constructor.newInstance(questionId, writer, title, contents, createdDate, countOfComment));
                }
            }
        } catch (Exception e) {
            logger.error("error", e);
        }
    }
}