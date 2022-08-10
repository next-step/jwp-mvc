package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

    @DisplayName("패키지의 Controller 애노테이션 찾기 테스트")
    @Test
    void findController() {
        ControllerScanner scanner = new ControllerScanner();
        Set<Class<?>> controller = scanner.findController("core.mvc.tobe");

        assertThat(controller).isNotEmpty();
    }
}