package core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    public static <T> T newInstance(Class<T> clazz, Object... args) {

        Constructor constructor = null;

        for (Constructor candidate : clazz.getConstructors()) {
            if (candidate.getParameterCount() == args.length) {
                constructor = candidate;
            }
        }

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

}
