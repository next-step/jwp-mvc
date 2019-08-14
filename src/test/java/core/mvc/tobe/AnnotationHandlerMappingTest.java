package core.mvc.tobe;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("GET 요청과 /users/show 패스를 가진 요청 처리")
    @Test
    public void getHandler_1() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/show");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);
        assertThat(modelAndView.getViewName()).isEqualTo("/users/show.jsp");
    }

    @DisplayName("GET 요청과 /users 패스를 가진 요청 처리")
    @Test
    public void getHandler_2() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);
        assertThat(modelAndView.getViewName()).isEqualTo("/users/list.jsp");
    }

    @DisplayName("POST 요청과 /users 패스를 가진 요청 처리")
    @Test
    public void getHandler_3() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView modelAndView = execution.handle(request, response);
        assertThat(modelAndView.getViewName()).isEqualTo("redirect:/users");


    }
}
