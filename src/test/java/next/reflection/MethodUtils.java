package next.reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MethodUtils {

    private static final Logger logger = LoggerFactory.getLogger(MethodUtils.class);

    public static void assertValidMethod(Method method, List<String> expectedNames) {
        assertTrue(expectedNames.contains(method.getName()));
    }

    public static void nonParameterMethodInvoke(Method method, Object target) {
        try {
            method.invoke(target);
        } catch (IllegalAccessException e) {
            logger.warn("{} method access failed.", method.getName());
        } catch (InvocationTargetException e) {
            logger.warn("{} invalid target.", target.toString());
        }
    }

}
