package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        // Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        // 모든 필드 정보
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(s -> logger.debug("Field Modifiers number : " + s.getModifiers()
                        + " / Field type : " + s.getType().getName()
                        + " / Field name : " + s.getName()));
        // 모든 생성자 정보
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(s -> {
                    List<String> parameterTypes = Arrays.stream(s.getParameterTypes())
                            .map(mn -> mn.getName())
                            .collect(Collectors.toList());
                    logger.debug("Constructor Modifiers number : " + s.getModifiers()
                            + " / Constructor name : " + s.getName()
                            + " / Constructor parameters type : " + parameterTypes.toString());
                });
        // 모든 메소드 정보
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(s -> {
                    List<String> parameterTypes = Arrays.stream(s.getParameterTypes())
                            .map(mn -> mn.getName())
                            .collect(Collectors.toList());
                    logger.debug("Method Modifiers number : " + s.getModifiers()
                            + " / Method return type : " + s.getReturnType().getName()
                            + " / Method name : " + s.getName()
                            + " / Method parameters type : " + parameterTypes.toString());
                });
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
    public void privateFieldAccess() throws Exception{
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        Field ageField = clazz.getDeclaredField("age");
        ageField.setAccessible(true);

        Student student = new Student();
        nameField.set(student, "rabbitfoot");
        ageField.set(student, 20);
        logger.debug("Student name : " + student.getName());
        logger.debug("Student age : " + student.getAge());
        logger.debug("Student toString : " + student.toString());
    }
}
