package core.mvc.tobe;

import next.controller.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {
    @DisplayName("생성해둔 인스턴스 불러오기")
    @Test
    void getInstance() {
        ControllerScanner cs = ControllerScanner.of("next.controller");
        Object userController = cs.getInstance(UserController.class);

        assertThat(userController instanceof UserController).isTrue();
    }
}