package core.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.mvc.tobe.ControllerExecution;
import core.mvc.tobe.HandlerExecutable;
import next.controller.UpdateUserController;
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

    @DisplayName("요청 uri와 일치하는 컨트롤러가 없는 경우 실행할 수 없다")
    @Test
    void cannot_execute_if_no_controller_matches_request_uri() {
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/not-exists");

        final HandlerExecutable actual = requestMapping.getHandler(request);

        assertThat(actual.executable()).isFalse();
    }

    @DisplayName("요청 uri와 일치하는 있는 경우 실행기를 반환한다")
    @Test
    void returns_the_controller_matching_request_uri() {
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/update");

        final HandlerExecutable actual = requestMapping.getHandler(request);

        final ControllerExecution expected = new ControllerExecution(new UpdateUserController());

        assertThat(actual).isEqualTo(expected);
    }

}
