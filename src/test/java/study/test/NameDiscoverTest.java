package study.test;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

public class NameDiscoverTest {

    private static final Logger log = LoggerFactory.getLogger(NameDiscoverTest.class);

    @Test
    void test() {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

        final Class<TestUserController> clazz = TestUserController.class;
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            final String[] parameterNames = nameDiscoverer.getParameterNames(method);
            log.debug("{} ======================", method.getName());
            for (String name : parameterNames) {
                log.debug("{}", name);
            }
            log.debug("=========================\n");

        }

    }
}
