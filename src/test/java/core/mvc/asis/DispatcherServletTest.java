package core.mvc.asis;

import core.mvc.HandlerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("Handler를 찾을 수 없을 경우 예외처리 한다.")
    @Test
    void handlerNotFound() {
        // given
        final HttpServletRequest request = new MockHttpServletRequest("POST", "/handler-not-found");
        final HttpServletResponse response = new MockHttpServletResponse();

        // when / then
        assertThatExceptionOfType(HandlerNotFoundException.class)
                .isThrownBy(() -> dispatcherServlet.service(request, response));
    }

    /**
     * @see core.mvc.asis.LegacyHandlerMapperAdapter
     * 에 있는 컨트롤러에 매핑한다.
     * migration 시 테스트 코드 같이 수정해야 함.
     */
    @DisplayName("레거시 매핑 핸들러인 유저폼 핸들링에 성공한다.")
    @Test
    void legacyMapping() throws Exception {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/form");
        final MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        dispatcherServlet.service(request, response);
        final String forwardUrl = response.getForwardedUrl();

        // then
        assertThat(forwardUrl).isEqualTo("/user/form.jsp");
    }

    /**
     * @see core.mvc.tobe.NextGenerationHandlerMapper
     * 에 있는 컨트롤러에 매핑한다.
     */
    @DisplayName("차세대 매핑 핸들러인 로그인 핸들러 매핑에 성공한다.")
    @Test
    void nextGenerationMapping() throws Exception {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/login");
        final MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        dispatcherServlet.service(request, response);
        final String forwardUrl = response.getForwardedUrl();

        // then
        assertThat(forwardUrl).isEqualTo("/user/login.jsp");
    }
}
