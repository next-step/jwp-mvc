package core.mvc.tobe;

import core.annotation.web.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentScannerTest {

    @Test
    @DisplayName("주어진 패키지 내에 Controller 어노테이션이 할당된 클래스를 스캔한다.")
    void doScan() {
        Object[] basePackage = new String[]{"core.mvc.tobe"};
        ComponentScanner componentScanner = ComponentScanner.of(basePackage);

        Map<Class<?>, Object> actual = componentScanner.doScan(Controller.class);

        assertThat(actual).hasSize(1);
        assertThat(actual).containsKeys(MyController.class);
    }
}
