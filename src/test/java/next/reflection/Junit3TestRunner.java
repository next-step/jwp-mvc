package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Junit3TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(Junit3TestRunner.class);

    @Test
    public void runMethod() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] methods = clazz.getMethods();
        Arrays.stream(methods)
                .forEach(m -> {
                    logger.debug("name: {}", m.getName());
                    final String methodName = m.getName();
                    try {
                        if (methodName.startsWith("test")) {
                            m.invoke(clazz.newInstance());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                });
    }

    @Test
    public void runDeclaredMethod() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
                .forEach(m -> {
                    logger.debug("name: {}", m.getName());
                    logger.debug("modifiers: {}", m.getModifiers());

                    final String methodName = m.getName();
                    try {
                        if (methodName.startsWith("test") && Modifier.isPublic(m.getModifiers())) {
                            m.invoke(clazz.newInstance());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                });
    }
}
