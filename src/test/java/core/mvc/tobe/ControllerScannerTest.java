package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    @DisplayName("지정한 패키지의 Controller 어노테이션이 붙은 클래스를 찾는다.")
    @Test
    void getController() {
        ControllerScanner controllerScanner = new ControllerScanner("core.mvc.tobe");

        Map<Class<?>, Object> controllerList = controllerScanner.getControllerMap();
        Set<Class<?>> controllerClassList = controllerList.keySet();

        assertThat(controllerClassList.size()).isEqualTo(1);
        assertThat(controllerClassList).contains(MyController.class);
    }

}
