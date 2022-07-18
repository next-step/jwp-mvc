package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerScannerTest {

    /**
     * Scan target controller - {@link MyController}
     */
    @Test
    void scan_Controller를_scan하여_HandlerExecution을_생성한다() {
        // when
        final Map<HandlerKey, HandlerExecution> scan = new ControllerScanner().scan("core.mvc.tobe");

        // then
        assertThat(scan)
                .hasSize(2)
                .containsKey(new HandlerKey("/users", RequestMethod.GET))
                .containsKey(new HandlerKey("/users", RequestMethod.POST));
    }
}
