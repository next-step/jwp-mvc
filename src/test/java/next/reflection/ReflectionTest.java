package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.Date;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("클래스의 정보를 투머치하게 보여줍시다")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        for (Field field : clazz.getDeclaredFields()) {
            printPrettified(field);
        }

        logger.info("=======================");

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            printPrettified(constructor);
        }

        logger.info("=======================");

        for (Method method : clazz.getDeclaredMethods()) {
            printPrettified(method);
        }

    }

    private void printPrettified(Field field) {
        logger.debug("Field -------------------------");
        logger.debug("field type: {}", field.getType());
        logger.debug("field name: {}", field.getName());
        logger.debug("modifier  : {}", Modifier.toString(field.getModifiers()));
        logger.debug("-------------------------------");
    }

    private void printPrettified(Constructor<?> constructor) {
        logger.debug("Constructor -------------------");
        final Class[] parameterTypes = constructor.getParameterTypes();
        logger.debug("constructor modifier: {}", Modifier.toString(constructor.getModifiers()));
        logger.debug("param length : {}", parameterTypes.length);
        for (Class paramType : parameterTypes) {
            logger.debug("param type : {}", paramType);
        }
        logger.debug("-------------------------------");
    }

    private void printPrettified(Method method) {
        logger.debug("Method ------------------------");
        logger.debug("method name: {}", method.getName());
        logger.debug("method modifier: {}", Modifier.toString(method.getModifiers()));
        logger.debug("method param count: {}", method.getParameterCount());
        for (Parameter parameter : method.getParameters()) {
            logger.debug("param type: {}", parameter.getType());
            logger.debug("param name: {}", parameter.getName());
        }
        logger.debug("-------------------------------");
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

    @DisplayName("private 필드에 접근해봅시당")
    @Test
    public void privateFieldAccess() throws Exception {
        final Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        final Student student = new Student();
        for (Field field : clazz.getDeclaredFields()) {
            injectValue(student, field, determineValueByFieldName(field));
        }
        logger.debug("result: {}", student);
    }

    private void injectValue(Student target, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(target, value);
    }

    private Object determineValueByFieldName(Field field) {
        final String name = field.getName();
        if (name.startsWith("name")) {
            return "이름 아닌 이름 같은거";
        } else if (name.startsWith("age")) {
            return 100;
        }
        return null;
    }

    @DisplayName("인자가 있는 생성자를 이용해 인스턴스를 생성해봐요!")
    @Test
    public void test_args_constructor() throws Exception {
        final Class<Question> clazz = Question.class;
        final Class<?>[] parameterTypes = {
                long.class, String.class, String.class, String.class, Date.class, int.class
        };
        final Constructor<Question> constructor = clazz.getDeclaredConstructor(parameterTypes);
        final Question question = constructor.newInstance(
                1L,
                "Chwon",
                "질문이 있습니다!",
                "오늘 출근 전에 아침을 사가고 싶은데 뭘 먹으면 좋을까요!",
                new Date(System.currentTimeMillis()),
                0);
        logger.debug("question: {}", question);
    }
}
