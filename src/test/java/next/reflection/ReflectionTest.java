package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        //Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
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
    public void constructor1() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();

        Arrays.stream(constructors)
                .forEach(c -> {
                    logger.debug("name: {}", c.toGenericString());
                    logger.debug("name: {}", c.getName());

                    assertThat(c.toGenericString()).contains(c.getName());

                    Class[] parameterTypes = c.getParameterTypes();
                    PrintHelper.printParameterTypes(parameterTypes);
                });
    }

    @Test
    public void method() throws Exception {
        Class<Question> clazz = Question.class;
        Method[] methods = clazz.getMethods();

        Arrays.stream(methods)
                .forEach(m -> {
                    logger.debug(m.toGenericString());
                    logger.debug(m.getName());

                    PrintHelper.printParameterTypes(m.getParameterTypes());

                    logger.debug("return type: {}", m.getReturnType());

                    assertThat(m.toGenericString()).contains(m.getName());
                    assertThat(m.toGenericString()).contains(m.getReturnType().getName());
                });
    }

    @Test
    public void field() throws Exception {
        Class<Question> clazz = Question.class;
        Field[] fields = clazz.getFields();

        logger.debug("field length: {}", fields.length);
        Arrays.stream(fields)
                .forEach(f -> {
                    logger.debug("name: {}", f.getName());
                });

    }

    @Test
    public void declaredField() throws Exception {
        Class<Question> clazz = Question.class;
        Field[] fields = clazz.getDeclaredFields();

        logger.debug("field length: {}", fields.length);
        Arrays.stream(fields)
                .forEach(f -> {
                    logger.debug(f.toGenericString());
                    logger.debug("name: {}", f.getName());
                    logger.debug("type: {}", f.getType());

                    assertThat(f.toGenericString()).contains(f.getName());
                    assertThat(f.toGenericString()).contains(f.getType().getName());
                });

    }

    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");

        nameField.setAccessible(true);
        ageField.setAccessible(true);

        Student student = clazz.newInstance();
        nameField.set(student, "길동");
        ageField.set(student, 30);

        String name = clazz.getMethod("getName").invoke(student).toString();
        int age = (int) clazz.getMethod("getAge").invoke(student);

        assertThat(name).isEqualTo(student.getName());
        assertThat(age).isEqualTo(student.getAge());
        assertThat(name).isEqualTo("길동");
        assertThat(age).isEqualTo(30);
    }


    @Test
    public void questionInstanceException() {
        Class<Question> clazz = Question.class;

        assertThatThrownBy(() -> {
            Question question = clazz.newInstance();
        }).isInstanceOf(InstantiationException.class);
    }

    @Test
    public void questionInstanceException1() {
        Class<Question> clazz = Question.class;

        assertThatThrownBy(() -> {
            Constructor<Question> constructor = clazz.getConstructor();
        }).isInstanceOf(NoSuchMethodException.class);
    }

    @Test
    public void questionInstanceException2() throws Exception {
        Class<Question> clazz = Question.class;

        Constructor[] constructors = clazz.getDeclaredConstructors();
        Constructor<Question> questionConstructor = clazz.getConstructor(constructors[0].getParameterTypes());

        assertThatThrownBy(() -> {
            Question question = questionConstructor.newInstance("0");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void questionInstance() throws Exception {
        Class<Question> clazz = Question.class;

        Constructor[] constructors = clazz.getDeclaredConstructors();
        Constructor<Question> questionConstructor = clazz.getConstructor(constructors[0].getParameterTypes());

        Question question = questionConstructor.newInstance("글쓴이", "제목", "내용");

        assertThat(question).isEqualTo(new Question("글쓴이", "제목", "내용"));
    }

    @Test
    public void questionInstance2() throws Exception {
        Class<Question> clazz = Question.class;

        Constructor[] constructors = clazz.getDeclaredConstructors();

        Date date = new Date();
        Constructor<Question> questionConstructor2 = clazz.getConstructor(constructors[1].getParameterTypes());
        Question question1 = questionConstructor2.newInstance(1, "글쓴이", "제목", "내용", date, 0);

        assertThat(question1).isEqualTo(new Question(1, "글쓴이", "제목", "내용", date, 0));
    }

}
