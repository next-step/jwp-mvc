package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerScannerTest {
    private static final Logger logger = LoggerFactory.getLogger(ControllerScannerTest.class);

    private ControllerScanner scanner;

    @Test
    public void getControllers() throws Exception {
        //given
        scanner = new ControllerScanner("core.mvc");
        //when
        Map<Class<?>, Object> controllers = scanner.getControllers();
        //then
        assertThat(controllers.get(MyController.class).getClass().getName()).isEqualTo(MyController.class.getName());
    }
}
