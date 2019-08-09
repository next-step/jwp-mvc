package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Junit3TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(Junit3TestRunner.class);
    private static final String PREFIX_TEST_METHOD = "test";

    @DisplayName("Junit3 테스트")
    @Test
    public void run() throws Exception {
        JunitUtils.findAndRunTestMethod(Junit3Test.class, logger, method -> method.getName().startsWith(PREFIX_TEST_METHOD));
    }
}
