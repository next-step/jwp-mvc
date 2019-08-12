package core.mvc.tobe;

import next.WebServerLauncher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setup() throws ClassNotFoundException {
        handlerMapping = new AnnotationHandlerMapping(WebServerLauncher.class);
        handlerMapping.initialize();
    }

    @Test
    void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }
}
