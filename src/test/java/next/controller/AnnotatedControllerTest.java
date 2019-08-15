package next.controller;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.stream.Stream;

public class AnnotatedControllerTest {
    private AnnotationHandlerMapping handlerMapping;

    private static Stream requestProvider() {
        return Stream.of(
                Arguments.of("/users", "GET")
        );
    }

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("next.controller");
        handlerMapping.initialize();
    }

    @DisplayName("@Controller 와 @RequestMapping 으로 설정하여 요청에 대한 응답")
    @ParameterizedTest(name = "RequestURI: {0}, RequestMethod: {1}")
    @MethodSource("requestProvider")
    public void getHandler(String requestURI, String method) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, requestURI);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        final ModelAndView modelAndView = execution.handle(request, response);
        Assertions.assertThat(modelAndView.getClass()).isEqualTo(ModelAndView.class);
    }

    @DisplayName("요청에 대한 핸들러를 찾을 수 없을 때 Null 리턴")
    @Test
    public void shouldNull_When_NotFound() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        Assertions.assertThat(execution).isNull();
    }
}
