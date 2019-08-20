package core.mvc.tobe;

import core.mvc.view.JspView;
import core.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MyControllerTest {

    private RequestMappingHandlerMapping handlerMapping;
    @BeforeEach
    void setUp() {
        handlerMapping = new RequestMappingHandlerMapping("core.mvc.tobe.MyController");
    }

    @DisplayName("url과 method별 요청시 반환되는 view name 확인")
    @ParameterizedTest
    @MethodSource(value = "uriTestCase")
    void viewName확인(String method, String path, String expected) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, path);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView mav = execution.handle(request, response);
        JspView jspView = (JspView) mav.getView();
        assertThat(jspView.getViewName()).isEqualTo(expected);
    }

    private static Stream<Arguments> uriTestCase() {
        return Stream.of(
                Arguments.of("GET", "/users", "/users/list.jsp"),
                Arguments.of("POST", "/users", "redirect:/users"),
                Arguments.of("GET", "/users/findUserId", "/users/show.jsp")
        );
    }

    @DisplayName("@RequestMapping에 method가 없으면 모든 method를 지원한다")
    @ParameterizedTest
    @MethodSource(value = "notExistMethodRequestTestCase")
    void notExistMethodRequestMapping(String method, String path) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, path);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        ModelAndView mav = execution.handle(request, response);
        JspView jspView = (JspView) mav.getView();
        assertThat(jspView.getViewName()).isEqualTo("/nothing");
    }

    private static Stream<Arguments> notExistMethodRequestTestCase() {
        return Stream.of(
                Arguments.of("GET", "/all"),
                Arguments.of("POST", "/all"),
                Arguments.of("PUT", "/all"),
                Arguments.of("DELETE", "/all")
        );
    }
}