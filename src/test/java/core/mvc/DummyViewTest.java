package core.mvc;

import core.mvc.view.DummyView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("더미 뷰")
class DummyViewTest {

    @Test
    @DisplayName("동치성 테스트")
    void equals() {
        assertThat(DummyView.INSTANCE)
                .isEqualTo(DummyView.INSTANCE);
    }
}
