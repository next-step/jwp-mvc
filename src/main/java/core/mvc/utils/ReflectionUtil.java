package core.mvc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class ReflectionUtil {

    private static final Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

    public static Object instantiateClass(Class<?> clazz) throws UnableToCreateInstanceException {
        return createInstance(determineNonArgsConstructor(clazz));
    }

    private static Constructor<?> determineNonArgsConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        return null;
    }

    private static Object createInstance(Constructor<?> constructor) throws UnableToCreateInstanceException {
        try {
            Objects.requireNonNull(constructor);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
            throw new UnableToCreateInstanceException("Unable to create instance!");
        }
    }
}
