package core.mvc.tobe.resolver;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class PathVariableMethodArgumentResolverTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
        handlerMapping.initialize();
    }

    @DisplayName("PathVariable 파라미터 맵핑이 성공한다.")
    @Test
    void pathVariable() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/30");

        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView handle = execution.handle(request, response);

        long id = (long) handle.getModel().get("id");

        assertThat(id).isEqualTo(30L);
    }
}
