package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
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
}
