package next.reflection;

import org.slf4j.Logger;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author : yusik
 * @date : 2019-08-09
 */
public class ReflectionUtils {

    static void findAndRunTestMethod(Class<?> clazz, Logger logger, Predicate<Method> methodPredicate) throws InstantiationException, IllegalAccessException {
        Object newInstance = clazz.newInstance();
        Stream.of(clazz.getMethods())
                .filter(methodPredicate)
                .forEach(method -> invokeTargetMethod(newInstance, method, logger));
    }

    private static void invokeTargetMethod(Object instance, Method method, Logger logger) {
        logger.debug("method name: {}", method.getName());
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error : {}", method.getName(), e);
        }
    }

    public static <T> void setField(Class<T> clazz, String fieldName, Object value, T newInstance) throws NoSuchFieldException, IllegalAccessException {
        Field name = clazz.getDeclaredField(fieldName);
        if (!name.isAccessible()) {
            name.setAccessible(true);
        }
        name.set(newInstance, value);
    }

    public static Object getTestInstance(Class<?> parameterType) throws Exception {
        if (parameterType.isPrimitive()) {
            return getDefaultValue(parameterType);
        }
        return parameterType.newInstance();
    }

    private static <T> T getDefaultValue(Class<T> clazz) {
        return (T) Array.get(Array.newInstance(clazz, 1), 0);
    }

    private static Object getPrimitiveValue(Class parameterType) {
        if (boolean.class == parameterType) {
            return Boolean.TRUE;
        } else if (short.class == parameterType) {
            return Short.MIN_VALUE;
        } else if (int.class == parameterType) {
            return Integer.MIN_VALUE;
        } else if (long.class == parameterType) {
            return Long.MIN_VALUE;
        } else if (float.class == parameterType) {
            return Float.MIN_VALUE;
        } else if (double.class == parameterType) {
            return Double.MIN_VALUE;
        }
        return Character.MIN_VALUE;
    }
}
