package next.reflection;

import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author : yusik
 * @date : 2019-08-09
 */
public class JunitUtils {

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
}
