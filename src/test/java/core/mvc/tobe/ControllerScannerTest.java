package core.mvc.tobe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ControllerScannerTest {
    @DisplayName("@Controller 클래스 읽어오는지 테스트")
    @Test
    public void test() {
        ControllerScanner controllerScanner = new ControllerScanner("core.mvc.tobe");
        Set<Class<?>> controllers = controllerScanner.getControllers();

        for (Class<?> controller : controllers) {
            Assertions.assertTrue(controller.getName().contains("Controller"));
        }
    }
}