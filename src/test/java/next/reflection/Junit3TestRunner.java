package next.reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Junit3TestRunner extends AbstractTestRunner {

    private static final String PREFIX_TEST_METHOD = "test";

    @Test
    public void run() throws Exception {
        super.run(Junit3Test.class);
    }

    @Override
    public boolean isTestMethod(Method method) {
        return method.getName().startsWith(PREFIX_TEST_METHOD);
    }
}
