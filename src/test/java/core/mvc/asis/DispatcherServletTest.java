package core.mvc.asis;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("디스패처 서블릿")
class DispatcherServletTest {
    private final static DispatcherServlet dispatcher = new DispatcherServlet();

    @BeforeAll
    static void init() throws ServletException {
        dispatcher.init();
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("jsp 뷰 서비스")
    void serviceGet(final String method, final String uri) throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcher.service(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getForwardedUrl()).isNotNull();
    }

    private static Stream<Arguments> serviceGet() {
        return Stream.of(
                Arguments.of("GET", "/users/profile/admin"), // @Controller
                Arguments.of("GET", "/") // implement Controller
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("302 redirect 서비스")
    void serviceRedirect(final String method, final String uri) throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcher.service(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FOUND);
        assertThat(response.getRedirectedUrl()).isNotNull();
    }

    private static Stream<Arguments> serviceRedirect() {
        return Stream.of(
                Arguments.of("GET", "/users/create"), // @Controller
                Arguments.of("GET", "/users/logout") // implement Controller
        );
    }

    @Test
    @DisplayName("존재하지 않는 url 에 대한 요청은 404 에러")
    void serviceNotFound() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/not-exist");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcher.service(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
    }
}
