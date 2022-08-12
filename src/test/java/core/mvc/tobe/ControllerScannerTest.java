package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @DisplayName("생성자로 주입받은 패키지 경로 이하에 @Controller 애노테이션이 붙은 클래스를 스캔뒤, 해당 클래스 타입의 인스턴스를 맵으로 저장한다.")
    @Test
    void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner("core.mvc.tobe");
        controllerScanner.initialize();

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        assertThat(controllers).hasSize(1);
        assertThat(controllers.get(MyController.class))
                .usingRecursiveComparison()
                .isEqualTo(new MyController());
    }
}