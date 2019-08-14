package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ControllerScannerTest {

    private ControllerScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new ControllerScanner();
    }

    @DisplayName("Controller Scan With ArgumentResolver")
    @Test
    void scan() {
        final Map<HandlerKey, HandlerExecution> scan = scanner.scan("next.mock");
        HandlerKey mockUser = new HandlerKey("/mock/users", RequestMethod.GET);
        HandlerKey mockQna = new HandlerKey("/mock/qna", RequestMethod.POST);

        assertNotNull(scan.get(mockUser));
        assertNotNull(scan.get(mockQna));
    }

}
