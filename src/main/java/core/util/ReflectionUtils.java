package core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    public static <T> T newInstance(Class<T> clazz, Object... args) {

        Constructor constructor = getConstructorByArgs(clazz, args);

        if (constructor == null) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " doesn't have args size constructor");
        }

        try {
            return clazz.cast(constructor.newInstance(args));
        } catch (IllegalAccessException e) {
            logger.warn("{} constructor access failed", constructor.getName());
        } catch(InvocationTargetException e) {
            logger.warn("{} target invalid", clazz.getSimpleName());
        } catch (InstantiationException e) {
            logger.warn("{} instantiation failed", clazz.getSimpleName());
        }

        throw new RuntimeException(clazz.getSimpleName() + " instantiation failed");
    }

    public static Constructor getConstructorByArgs(Class clazz, Object... args) {
        for (Constructor candidate : clazz.getConstructors()) {
            if (isMatched(candidate, args)) {
                return candidate;
            }
        }

        return null;
    }

    public static boolean isMatched(Constructor constructor, Object... args) {

        if (constructor.getParameterCount() != args.length) {
            return false;
        }

        final Class[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            if (parameterTypes[i] != args[i].getClass()) {
                return false;
            }
        }

        return true;
    }

}
