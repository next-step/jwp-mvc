package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationHandlerAdapterTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setup() {
        handlerMapping = new AnnotationHandlerMapping("next.mock");
        handlerMapping.initialize();
    }

    @DisplayName("AnnotationHandlerAdapter request mapping test")
    @Test
    void getHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/mock/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        final Object result = execution.handle(request, response);
        ModelAndView mav = (ModelAndView) result;
        assertThat(mav.getViewName()).isEqualTo("forward:users.jsp");
        assertThat(request.getAttribute("mock")).isEqualTo("users");

    }
}
