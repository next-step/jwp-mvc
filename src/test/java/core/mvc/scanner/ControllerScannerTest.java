package core.mvc.scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ControllerScannerTest {

    private static final Logger log = LoggerFactory.getLogger(ControllerScannerTest.class);

    @DisplayName("컨트롤러 스캐너가 타입을 이용해 잘 스캔하는지 테스트")
    @Test
    void test_controller_scan_with_object() {
        final ControllerScanner scanner = new ControllerScanner(this.getClass());
        final Object instance = scanner.getInstance(ControllerScannerTest.class);
        assertNotEquals(null, instance);
        log.debug("instance: {}", instance);
    }

    @DisplayName("컨트롤러 스캐너가 패키지 경로를 이용해 잘 스캔하는지 테스트")
    @Test
    void test_controller_scan_with_base_package_name() {
        final ControllerScanner scanner = new ControllerScanner("core.mvc.scanner");
        final Object instance = scanner.getInstance(ControllerScannerTest.class);
        assertNotEquals(null, instance);
        log.debug("instance: {}", instance);
    }
}
