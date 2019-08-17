package core.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

class DispatcherServletTest {

    private final DispatcherServlet dispatcherServlet = new DispatcherServlet();

    @BeforeEach
    void setUp() {
        dispatcherServlet.init();
    }

    @DisplayName("요청이 매칭되는 서블릿이 없을 시 404에러를 반환한다")
    @Test
    void service_isNotExistRequestServlet_exception() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/nouri");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(SC_NOT_FOUND);
    }
}