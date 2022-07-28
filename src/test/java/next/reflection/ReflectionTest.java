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

        //  모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
        logger.debug("클래스 이름 : {}", clazz.getName());
        for (Field fieldName : clazz.getFields()) {
            logger.debug("필드이름 : {}", fieldName.getName());
        }

        for (Constructor constructor : clazz.getConstructors()) {
            logger.debug("생성자 이름 : {}", constructor.getName());
        }

        for (Method method : clazz.getMethods()) {
            logger.debug("메서드 이름 : {}", method.getName());
        }
    }

    @Test
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        logger.debug("클래스 이름 : {}", clazz.getName());

        try {
            Student student = new Student();
            setField(clazz, "name", student, "재성");
            setField(clazz, "age", student, 48);

            logger.debug("name : {}", student.getName());
            logger.debug("age : {}", student.getAge());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    private void setField(Class<Student> clazz, String fieldName, Object instance, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field nameField = clazz.getDeclaredField(fieldName);
        nameField.setAccessible(true);
        nameField.set(instance, value);
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
