package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

class ControllerScannerTest {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScannerTest.class);

    @DisplayName("@Controller 클래스 읽어오는지 테스트")
    @Test
    public void test() {
        ControllerScanner controllerScanner = new ControllerScanner("core.mvc.tobe");
        Set<Class<?>> controllers = controllerScanner.getControllers();

        controllers
                .forEach(clazz -> logger.debug("class : {}", clazz.getName()));
    }

}