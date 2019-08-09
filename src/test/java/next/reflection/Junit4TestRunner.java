package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Junit4TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(Junit4TestRunner.class);

    @DisplayName("Junit4 테스트")
    @Test
    public void run() throws Exception {
        JunitUtils.findAndRunTestMethod(Junit4Test.class, logger, method -> method.getAnnotation(MyTest.class) != null);
    }
}
