package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerCollectorTest {
    @DisplayName("패키지 목록에서 컨트롤러 어노테이션이 지정된 클래스의 객체 목록을 반환")
    @Test
    void collect() {
        final Set<Object> controllers = ControllerCollector.collect("core.mvc.tobe");
        assertThat(controllers).hasAtLeastOneElementOfType(MyController.class);
    }
}
