package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private static final Map<Class<?>, Object> REFERENCE_TYPE_VALUES;

    static {
        REFERENCE_TYPE_VALUES = new HashMap<>();

        REFERENCE_TYPE_VALUES.put(String.class, "TEXT");
        REFERENCE_TYPE_VALUES.put(int.class, 0);
        REFERENCE_TYPE_VALUES.put(long.class, 0L);
        REFERENCE_TYPE_VALUES.put(Date.class, new Date(0));
    }

    @Test
    @DisplayName("Question 클래스의 필드, 생성자, 메소드 를 모두 출력")
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        // field
        logger.debug("FIELDS================================");
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> {
                    logger.debug(field.toString());
                    logger.debug("{} {} {};\n",
                            Modifier.values()[field.getModifiers()],
                            simplify(field.getType()),
                            field.getName());
                });

        // constructor
        logger.debug("Constructor===========================");
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(constructor -> {
                    logger.debug(constructor.toString());
                    logger.debug("{} {}({})\n",
                            Modifier.values()[constructor.getModifiers()],
                            getClassNameOnly(constructor.getName()),
                            removeSquareBrackets(simplify(constructor.getGenericParameterTypes())));
                });

        // method
        logger.debug("Method================================");
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(method -> {
                    logger.debug(method.toString());
                    logger.debug("{} {}({})\n",
                            Modifier.values()[method.getModifiers()],
                            getClassNameOnly(method.getName()),
                            removeSquareBrackets(simplify(method.getGenericParameterTypes())));
                });
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("param length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }

    private static String simplify(final Type type) {
        String typeValue = type.getTypeName();

        return getClassNameOnly(typeValue);
    }

    private static String getClassNameOnly(final String packagedAttachedName) {
        if (!packagedAttachedName.contains(".")) {
            return packagedAttachedName;
        }

        return packagedAttachedName.substring(packagedAttachedName.lastIndexOf('.') + 1);
    }

    private static List<String> simplify(final Type[] types) {
        return Arrays.stream(types)
                .map(ReflectionTest::simplify)
                .collect(Collectors.toList());
    }

    private static String removeSquareBrackets(List<String> origin) {
        return origin.toString()
                .replace("[", "")
                .replace("]", "");
    }

    @Test
    @DisplayName("private 필드에 값 할당하기")
    void injectFieldValue() {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();

        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> setPrivateFieldValue(student, field));

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("get"))
                .forEach(method -> invokeMethod(student, method));

        logger.debug("{}", student);
    }

    private void invokeMethod(final Object object, final Method method) {
        try {
            assertThat(method.invoke(object)).isEqualTo(REFERENCE_TYPE_VALUES.get(method.getReturnType()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Fail to invoke method : {}", e.getMessage());
        }
    }

    private void setPrivateFieldValue(final Student student, final Field field) {
        try {
            field.setAccessible(true);
            field.set(student, REFERENCE_TYPE_VALUES.get(field.getType()));
        } catch (IllegalAccessException e) {
            logger.error("Fail to set value at private field : {}", e.getMessage());
        }
    }

    @Test
    @DisplayName("인자가 있는 생성자로 인스턴스 생성하기")
    void createInstanceWithArguments() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Arrays.stream(constructors)
                .map(this::createInstance)
                .forEach(instance -> {
                    invokeAllGetterMethod(clazz, instance);

                    logger.debug("{}", instance);
                });
    }

    private void invokeAllGetterMethod(final Class<?> clazz, final Object object) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("get"))
                .filter(method -> !method.getName().contains("Date"))
                .forEach(method -> invokeMethod(object, method));
    }

    private <T> T createInstance(final Constructor<T> constructor) {
        List<Object> arguments = Arrays.stream(constructor.getParameterTypes())
                .map(REFERENCE_TYPE_VALUES::get)
                .collect(Collectors.toList());

        try {
            return constructor.newInstance(arguments.toArray(new Object[arguments.size()]));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Fail to create instance : {}", e.getMessage());
            return null;
        }
    }
}
