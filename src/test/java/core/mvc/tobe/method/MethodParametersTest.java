package core.mvc.tobe.method;

import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("메소드 파라미터 일급 컬렉션")
class MethodParametersTest {

    @Test
    @DisplayName("메서드로 생성")
    void instance() {
        assertThatNoException()
                .isThrownBy(() -> MethodParameters.from(
                        MethodParametersTest.class.getDeclaredMethod("singleLongUserIdArgument", Long.TYPE)));
    }

    @Test
    @DisplayName("메서드는 필수")
    void instance_null_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MethodParameters.from(null));
    }

    private ModelAndView singleLongUserIdArgument(long userId) {
        return ModelAndView.of(Map.of("userId", userId), new MockView());
    }
}
