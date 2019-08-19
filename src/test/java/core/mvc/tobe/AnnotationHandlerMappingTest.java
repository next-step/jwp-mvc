package core.mvc.tobe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setup() throws ClassNotFoundException {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @Test
    void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @Test
    void createString() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/string");
        request.addParameter("userId", "javajigi");
        request.addParameter("password", "password");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @Test
    void createIntLong() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/primitive");
        request.addParameter("id", "9");
        request.addParameter("age", "10");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @Test
    void createJavabean() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/beans");
        MockHttpServletResponse response = new MockHttpServletResponse();

        TestUser testUser = new TestUser("javajigi", "password", 10);
        request.addParameter("userId", testUser.getUserId());
        request.addParameter("password", testUser.getPassword());
        request.addParameter("age", String.valueOf(testUser.getAge()));

        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @Test
    void pathVariable() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }
}
