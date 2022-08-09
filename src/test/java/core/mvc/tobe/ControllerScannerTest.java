package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("컨트롤러 탐지기")
class ControllerScannerTest {

    public static final String TOBE_BASE_PACKAGE = "core.mvc.tobe";

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException().isThrownBy(() -> ControllerScanner.from(TOBE_BASE_PACKAGE));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("기본 패키지는 필수")
    void instance_nullOrEmpty_thrownIllegalArgumentException(String nullOrEmpty) {
        assertThatIllegalArgumentException().isThrownBy(() -> ControllerScanner.from(nullOrEmpty));
    }

    @Test
    @DisplayName("컨트롤러 리스트 가져오기")
    void controllers() {
        //given
        ControllerScanner toBePackageControllerScanner = ControllerScanner.from(TOBE_BASE_PACKAGE);
        //when, then
        assertThat(toBePackageControllerScanner.controllers())
                .extractingByKey(MyController.class)
                .isInstanceOf(MyController.class);
    }

    @Test
    @DisplayName("클래스로 컨트롤러 객체 가져오기")
    void getInstance() {
        //given
        ControllerScanner toBePackageControllerScanner = ControllerScanner.from(TOBE_BASE_PACKAGE);
        //when, then
        assertThat(toBePackageControllerScanner.instance(MyController.class))
                .containsInstanceOf(MyController.class);
    }
}
