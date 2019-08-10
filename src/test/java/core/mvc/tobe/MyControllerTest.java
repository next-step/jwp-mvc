package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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

    @DisplayName("method type이 default인 경우 get mapping 처리하는지")
    @Test
    public void default_request_method_get() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @DisplayName("method type이 default인 경우 put mapping 처리하는지")
    @Test
    public void default_request_method_put() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @DisplayName("method type이 default인 경우 post mapping 처리")
    @Test
    public void default_request_method_post() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @DisplayName("method typd이 post인 경우 post mapping 처리")
    @Test
    public void request_method_post() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @DisplayName("method typd이 get인 경우 get mapping 처리")
    @Test
    public void request_method_get() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/show");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}