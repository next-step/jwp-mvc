package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @DisplayName("ContollerScanner 는 특정 패키지에 있는 Controller 를 찾아낼 수 있다.")
    @Test
    void scanTest() {
        // given
        ControllerScanner controllerScanner = new ControllerScanner("core.mvc.tobe");

        // when
        ControllerRegistry registry = controllerScanner.getControllerRegistry();

        // then
        assertThat(registry.getInstanceByType(MyController.class))
                .isNotNull();
        assertThat(registry.getInstanceByType(TestUserController.class))
                .isNull();
    }

}
