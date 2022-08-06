package core.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.mvc.tobe.ControllerExecution;
import core.mvc.tobe.HandlerExecutable;
import next.controller.HomeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class RequestMappingTest {

    private RequestMapping requestMapping;

    @BeforeEach
    void setUp() {
        requestMapping = new RequestMapping();
        requestMapping.initMapping();
    }

    @DisplayName("요청 uri와 일치하는 컨트롤러가 없는 경우 예외를 발생한다")
    @Test
    void exception_if_no_controller_matches_request_uri() {
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/not-exists");

        assertThatThrownBy(() -> requestMapping.getHandler(request)).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("요청 uri에 해당하는 컨트롤러가 없습니다: /not-exists");
    }

    @DisplayName("요청 uri와 일치하는 있는 경우 실행기를 반환한다")
    @Test
    void returns_the_controller_matching_request_uri() {
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");

        final HandlerExecutable actual = requestMapping.getHandler(request);

        final ControllerExecution expected = new ControllerExecution(new HomeController());

        assertThat(actual).isEqualTo(expected);
    }

}
