package core.mvc.tobe;

import core.annotation.web.Controller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

class ControllerScannerTest {
    @DisplayName("@Controller 클래스 읽어오는지 테스트")
    @Test
    public void test() {
        ControllerScanner controllerScanner = new ControllerScanner("core.mvc.tobe");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Class<?> controller : controllers.keySet()) {
            Assertions.assertTrue(controller.isAnnotationPresent(Controller.class));
        }
    }
}