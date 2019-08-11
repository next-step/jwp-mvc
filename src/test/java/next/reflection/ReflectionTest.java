package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    public static Question newQuestion() {
        return new Question("홍길동", "홍길동전", "내용..");
    }

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            printParameterTypes(parameterTypes);
        }
    }

    private void printParameterTypes(Class[] parameterTypes) {
        logger.debug("paramer length : {}", parameterTypes.length);
        for (Class paramType : parameterTypes) {
            logger.debug("param type : {}", paramType);
        }
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void method() throws Exception {
        Class<Question> clazz = Question.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            logger.debug("name: " + method.getName());
            logger.debug("modifier: " + method.getModifiers());
            logger.debug("returntype: " + method.getReturnType());
            printParameterTypes(method.getParameterTypes());

        }
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void fields() throws Exception {
        Class<Question> clazz = Question.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            logger.debug("name: " + field.getName());
            logger.debug("type: " + field.getType());
        }
    }


    //자바 Reflection API를 활용해 다음 Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.
    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();

        Field nameField = getAccessibleField(clazz, "name");
        nameField.set(student, "길동");

        Field ageField = getAccessibleField(clazz, "age");
        ageField.set(student, 20);

        assertThat(student.getName()).isEqualTo("길동");
        assertThat(student.getAge()).isEqualTo(20);

    }

    private Field getAccessibleField(Class<Student> clazz, String filedName) throws NoSuchFieldException {
        Field nameField = clazz.getDeclaredField(filedName);
        nameField.setAccessible(true);
        return nameField;
    }

    // Qutions 클래스의 인스턴스를 자바 Reflection API를 활용해 Question 인스턴스를 생성한다.
    @Test
    public void create_class_no_constructor() throws IllegalAccessException, InstantiationException {
        Class<Question> clazz = Question.class;

        assertThrows(InstantiationException.class, () -> {
            clazz.newInstance();
        });
    }

    @Test
    public void create_class() throws Exception {
        Class<Question> clazz = Question.class;

        Question question = newQuestion();
        Question otherQuestion = null;

        Constructor constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);
        otherQuestion = (Question) constructor.newInstance(question.getWriter(), question.getTitle(), question.getContents());

        assertThat(otherQuestion).isEqualTo(question);
    }

}
