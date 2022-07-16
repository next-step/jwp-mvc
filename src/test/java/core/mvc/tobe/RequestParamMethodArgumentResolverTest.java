package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RequestParamMethodArgumentResolverTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("String 타입 파라미터 맵핑이 정상적으로 동작한다.")
    @Test
    void string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/string");
        request.setParameter("userId", "dean");
        request.setParameter("password", "password");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView handle = execution.handle(request, response);

        Map<String, Object> model = handle.getModel();
        String userId = (String) model.get("userId");
        String password = (String) model.get("password");

        assertThat(userId).isEqualTo("dean");
        assertThat(password).isEqualTo("password");
    }

    @DisplayName("request_param 타입 파라미터 맵핑이 정상적으로 동작한다.")
    @Test
    void request_param() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/request_param");
        request.setParameter("id", "dean");
        request.setParameter("value", "password");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView handle = execution.handle(request, response);

        Map<String, Object> model = handle.getModel();
        String userId = (String) model.get("userId");
        String password = (String) model.get("password");

        assertThat(userId).isEqualTo("dean");
        assertThat(password).isEqualTo("password");
    }

    @DisplayName("primitive 타입 파라미터 맵핑이 정상적으로 동작한다.")
    @Test
    void primitive() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/primitive");
        request.setParameter("id", "1");
        request.setParameter("age", "30");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView handle = execution.handle(request, response);

        Map<String, Object> model = handle.getModel();
        long id = (long) model.get("id");
        int age = (int) model.get("age");

        assertThat(id).isEqualTo(1L);
        assertThat(age).isEqualTo(30);
    }

}
