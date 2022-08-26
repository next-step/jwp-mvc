package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUserControllerTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("String을 인자로 받는 create_string 메서드를 실행시킨다.")
    @Test
    void create_string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.addParameter("userId", "javajigi");
        request.addParameter("password", "password");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getModel().get("userId")).isEqualTo("javajigi");
        assertThat(modelAndView.getModel().get("password")).isEqualTo("password");
    }

    @DisplayName("int, long을 인자로 받는 create_int_long 메서드를 실행시킨다.")
    @Test
    void create_int_long() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/int-long");
        request.addParameter("id", "1");
        request.addParameter("age", "20");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getModel().get("id")).isEqualTo(1L);
        assertThat(modelAndView.getModel().get("age")).isEqualTo(20);
    }

    @DisplayName("testUser을 인자로 받는 create_javabean 메서드를 실행시킨다.")
    @Test
    void create_javabean() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/javabean");
        TestUser testUser = new TestUser("pobi", "password", 20);
        request.setAttribute("testUser", testUser);
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getModel().get("testUser")).isEqualTo(testUser);
    }

    @DisplayName("path variable을 사용하는 show_pathvariable 메서드를 실행시킨다.")
    @Test
    void show_pathvariable() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);

        assertThat(modelAndView.getModel().get("id")).isEqualTo(1L);
    }
}
