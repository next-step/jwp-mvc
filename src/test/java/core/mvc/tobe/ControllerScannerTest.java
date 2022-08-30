package core.mvc.tobe;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ControllerScannerTest {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScannerTest.class);

    private ControllerScanner scanner;

    @Test
    public void getControllers() throws Exception {
        scanner = new ControllerScanner("core.mvc");
        Map<Class<?>, Object> controllers = scanner.getControllers();
        for (Class<?> controller : controllers.keySet()){
            logger.debug("controller : {}", controller);
        }
    }
}
