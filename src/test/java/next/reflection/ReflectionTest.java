package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void showClass() {
        Class<Question> clazz = Question.class;

        /**
         *  클래스, 모든 필드, 생성자, 메소드에 대한 정보
         */
        logger.debug(clazz.getName());

        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            logger.debug("## field name: {}, field type: {}", field.getName(), field.getType());
        }

        Constructor<?>[] constructors = clazz.getConstructors();

        for (Constructor<?> constructor : constructors) {
            logger.debug("## constructor parameter count: {}", constructor.getParameterCount());
        }

        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            logger.debug("## method name: {}", method.getName());
            logger.debug("## method return type: {}", method.getReturnType());
        }

    }

    @Test
    void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Student student = new Student();
        Class<Student> studentClass = Student.class;

        Field nameField = studentClass.getDeclaredField("name");
        Field ageField = studentClass.getDeclaredField("age");

        nameField.setAccessible(true);
        ageField.setAccessible(true);

        nameField.set(student, "hiro");
        ageField.set(student, 10);

        assertThat(nameField.get(student)).isEqualTo("hiro");
        assertThat(ageField.get(student)).isEqualTo(10);
    }

    @Test
    void new_instance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?>[] declaredConstructors = Question.class.getDeclaredConstructors();

        Class<Question> questionClass = Question.class;

        Constructor<Question> constructor = questionClass.getConstructor(String.class, String.class, String.class);

        String name = "my name";
        String title = "my title";
        String content = "my content";

        Question question = constructor.newInstance(name, title, content);

        assertThat(question.getWriter()).isEqualTo(name);
        assertThat(question.getTitle()).isEqualTo(title);
        assertThat(question.getContents()).isEqualTo(content);

    }

    @Test
    @SuppressWarnings("rawtypes")
    void constructor() throws Exception {
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
