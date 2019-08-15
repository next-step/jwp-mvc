package core.utils;

import core.mvc.tobe.ModelAndViewWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtils {
    private static final Logger logger = LoggerFactory.getLogger(ModelAndViewWrapper.class);

    public static Object getInstance(Class<?> aClass, Object... argument) throws ReflectiveOperationException {
        try {
            Class<?>[] parameterTypes = getArgumentParameterTypes(argument);
            return aClass.getDeclaredConstructor(parameterTypes).newInstance(argument);
        } catch (ReflectiveOperationException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private static Class<?>[] getArgumentParameterTypes(Object[] argument) {
        Class<?>[] classes = new Class[argument.length];
        for (int i = 0; i < argument.length; i++) {
            classes[i] = argument[i].getClass();
        }
        return classes;
    }
}
