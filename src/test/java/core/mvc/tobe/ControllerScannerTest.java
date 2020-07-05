package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ControllerScannerTest {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScannerTest.class);

    private ControllerScanner cs;

    @BeforeEach
    public void setup() {
        cs = new ControllerScanner("core.mvc");
    }

    @Test
    public void getController() {
        Map<Class<?>, Object> controllers = cs.getControllers();
        for(Class<?> controller: controllers.keySet()) {
            logger.debug("contorller : {}", controller);
        }
    }
}
