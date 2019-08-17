package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.stream.Stream;

public class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
    }

    @ParameterizedTest
    @MethodSource(value = "handlerTestCase")
    void getHandler(String method, String url) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, url);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    private static Stream<Arguments> handlerTestCase() {
        return Stream.of(
                Arguments.of("GET", "/users/findUserId"),
                Arguments.of("POST", "/users")
        );
    }

}
