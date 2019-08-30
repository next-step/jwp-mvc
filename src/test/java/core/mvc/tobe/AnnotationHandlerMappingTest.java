package core.mvc.tobe;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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

        final HandlerExecution execution = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = execution.handle(request, new MockHttpServletResponse());

        assertThat(modelAndView.getView()).isEqualTo(new JspView("/users/list.jsp"));
    }

    @Test
    void createString() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/string");
        request.addParameter("userId", "javajigi");
        request.addParameter("password", "password");

        final ModelAndView modelAndView = createModelAndView(request);

        final Map<String, Object> model = modelAndView.getModel();

        assertThat(model.get("userId")).isEqualTo("javajigi");
        assertThat(model.get("password")).isEqualTo("password");
    }

    @Test
    void createIntLong() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/primitive");
        request.addParameter("id", "9");
        request.addParameter("age", "10");

        final ModelAndView modelAndView = createModelAndView(request);

        final Map<String, Object> model = modelAndView.getModel();

        assertThat(model.get("id")).isEqualTo(9L);
        assertThat(model.get("age")).isEqualTo(10);
    }

    @Test
    void createJavabean() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/beans");

        TestUser testUser = new TestUser("javajigi", "password", 10);
        request.addParameter("password", testUser.getPassword());
        request.addParameter("age", String.valueOf(testUser.getAge()));
        request.addParameter("userId", testUser.getUserId());

        final ModelAndView modelAndView = createModelAndView(request);

        final TestUser actual = (TestUser) modelAndView.getObject("testUser");

        assertThat(actual.getUserId()).isEqualTo(testUser.getUserId());
        assertThat(actual.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(actual.getAge()).isEqualTo(testUser.getAge());
    }

    @Test
    void pathVariable() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/1");

        final ModelAndView modelAndView = createModelAndView(request);

        final Map<String, Object> model = modelAndView.getModel();

        assertThat(model.get("id")).isEqualTo(1L);
    }

    private ModelAndView createModelAndView(MockHttpServletRequest request) throws Exception {
        final HandlerExecution execution = handlerMapping.getHandler(request);
        return execution.handle(request, new MockHttpServletResponse());
    }
}
