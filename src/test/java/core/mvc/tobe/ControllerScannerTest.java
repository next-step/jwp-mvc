package core.mvc.tobe;

import next.controller.CreateUserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerScannerTest {
    private static final String BASE_PACKAGE_PATH = "next.controller";

    private ControllerScanner controllerScanner;

    @BeforeEach
    void setUp() {
        Reflections reflections = new Reflections(BASE_PACKAGE_PATH, Scanners.TypesAnnotated);
        controllerScanner = new ControllerScanner(reflections);
    }

    @DisplayName("ControllerScanner 클래스를 생성합니다.")
    @Test
    void construct() {
        assertThat(controllerScanner).isNotNull();
    }

    @DisplayName("@Controller 애노테이션이 붙은 클래스를 ControllerMap에 담고, 원하는 클래스를 잘 가져왔는지 확인한다.")
    @Test
    void getControllers() {
        Map<Class<?>, Object> controllerMap = controllerScanner.getControllers();
        assertThat(controllerMap.get(CreateUserController.class).getClass().getName()).isEqualTo(CreateUserController.class.getName());
    }
}
