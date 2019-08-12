package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyControllerTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("method type이 default인 경우에 대한 테스트")
    @ParameterizedTest
    @CsvSource({"GET, /users/findUserId, 200", "POST, /users/findUserId, 200",
            "PUT, /users/findUserId, 200", "DELETE, /users/findUserId, 200"})
    public void request_default_method_test(String method, String uri, int status) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertEquals(status, response.getStatus());
    }

    @DisplayName("method type이 지정된 경우에 대한 테스트")
    @ParameterizedTest
    @CsvSource({"GET, /users/show, 200",
            "POST, /users, 200"})
    public void request_method_test(String method, String uri, int status) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertEquals(status, response.getStatus());
    }
}