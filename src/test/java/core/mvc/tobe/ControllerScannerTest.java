package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.mvc.tobe.support.HttpRequestArgumentResolver;
import core.mvc.tobe.support.HttpResponseArgumentResolver;
import core.mvc.tobe.support.RequestParamArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ControllerScannerTest {

    private ControllerScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new ControllerScanner(asList(
                new HttpRequestArgumentResolver(),
                new HttpResponseArgumentResolver(),
                new RequestParamArgumentResolver()
        ));
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
