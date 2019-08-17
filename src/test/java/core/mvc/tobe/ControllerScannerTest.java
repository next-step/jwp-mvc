package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    private static final String DEFAULT_PACKAGE = "core.mvc.tobe";
    private ControllerScanner controllerScanner;

    @BeforeEach
    void setUp() {
        controllerScanner = new ControllerScanner(DEFAULT_PACKAGE);
    }

    @DisplayName(DEFAULT_PACKAGE + "의 Controller 어노테이션이 붙은 객체를 생성하는데 성공한다")
    @Test
    void getControllers_success() {
        // when
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // then
        assertThat(controllers).hasSize(1);
        assertThat(controllers.keySet()).contains(MyController.class);
    }
}