package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ControllerScannerTest {

    @DisplayName("Controller 애노테이션이 설정된 클래스 반환")
    @Test
    void scan() {

        //given
        String basePackage = "core.mvc.tobe";

        // when
        Set<Class<?>> controllers = ControllerScanner.scan(basePackage);

        // then
        assertThat(controllers)
                .containsOnly(MyController.class);
    }
}