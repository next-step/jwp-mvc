package core.mvc.tobe;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ControllerScannerTest {

    @Test
    void get_controllers() {
        final ControllerScanner controllerScanner = new ControllerScanner("core.mvc.tobe");
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        final Set<Class> expected = Set.of(MyController.class);
        assertEquals(expected, controllers.keySet());
    }
}
