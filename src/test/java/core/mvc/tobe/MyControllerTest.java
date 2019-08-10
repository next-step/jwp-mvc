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

    @DisplayName("MyController의 /users/findUserId로 요청시 status가 200인지")
    @Test
    public void getHandler_get() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @DisplayName("MyController의 /users로 요청시 status가 200인지")
    @Test
    public void getHandler_post() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}