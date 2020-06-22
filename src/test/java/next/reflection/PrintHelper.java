package next.reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class PrintHelper {
    private static final Logger logger = LoggerFactory.getLogger(PrintHelper.class);

    public static void printParameterTypes(Class[] parameterTypes) {
        logger.debug("parameter length: {}", parameterTypes.length);
        Arrays.stream(parameterTypes)
                .forEach(p -> {
                    logger.debug("parameter type: {}", p.getName());
                });
    }
}
