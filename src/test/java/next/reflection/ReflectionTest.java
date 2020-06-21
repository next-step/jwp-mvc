package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

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
}
