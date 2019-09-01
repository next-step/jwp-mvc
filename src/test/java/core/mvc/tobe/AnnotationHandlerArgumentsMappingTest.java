package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class AnnotationHandlerArgumentsMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("String 매개변수")
    @Test
    public void string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/userList");
        request.addParameter("userId", "kohyusik");
        request.addParameter("password", "1234");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @DisplayName("primitive 매개변수")
    @Test
    public void primitive() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        request.addParameter("id", "1");
        request.addParameter("age", "30");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @DisplayName("object 매개변수")
    @Test
    public void getHandlerMultiple() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.addParameter("userId", "kohyusik");
        request.addParameter("password", "1234");
        request.addParameter("age", "30");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @DisplayName("path variable 매개변수")
    @Test
    public void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/99");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }
}
