package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class TestUserControllerTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("인자가 String인 경우")
    @Test
    public void method_argument_string_test() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/string");
        request.addParameter("userId", "Summer");
        request.addParameter("password", "pw");

        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView handle = execution.handle(request, response);

        Assertions.assertEquals("Summer", handle.getObject("userId"));
        Assertions.assertEquals("pw", handle.getObject("password"));
    }

    @DisplayName("인자가 int/long 경우")
    @Test
    public void method_argument_int_long_test() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/nonstring");
        request.addParameter("id", "123");
        request.addParameter("age", "28");

        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView handle = execution.handle(request, response);

        Assertions.assertEquals(123l, handle.getObject("id"));
        Assertions.assertEquals(28, handle.getObject("age"));
    }

    @DisplayName("인자가 javabean인 경우")
    @Test
    public void method_argument_java_bean_test() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/javabean");
        request.addParameter("userId", "Summer");
        request.addParameter("password", "pw");
        request.addParameter("age", "28");

        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView handle = execution.handle(request, response);

        TestUser user = (TestUser) handle.getObject("testUser");

        Assertions.assertEquals("Summer", user.getUserId());
        Assertions.assertEquals("pw", user.getPassword());
        Assertions.assertEquals(28, user.getAge());
    }

    @DisplayName("path variable이 있는 경우")
    @Test
    public void method_argument_with_path_variable_test() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/1");

        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView handle = execution.handle(request, response);

        Assertions.assertEquals(1l, handle.getObject("id"));
    }
}