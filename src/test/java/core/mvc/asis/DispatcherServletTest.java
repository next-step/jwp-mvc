package core.mvc.asis;

import core.mvc.HandlerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        final HttpServletRequest request = new MockHttpServletRequest("POST", "/handler-not-found");
        final HttpServletResponse response = new MockHttpServletResponse();

        assertThatExceptionOfType(HandlerNotFoundException.class)
                .isThrownBy(() -> dispatcherServlet.service(request, response));
    }
}
