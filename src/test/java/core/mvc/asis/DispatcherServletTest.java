package core.mvc.asis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class DispatcherServletTest {
    private static final String HEADER_LOCATION_KEY = "Location";
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @ParameterizedTest
    @MethodSource("provideRequest")
    @DisplayName("DispatcherServlet 이 정상 동작하는지 확인한다.")
    public void service(String method, String uri, String result) throws ServletException {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(method, uri);
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

        dispatcherServlet.service(httpServletRequest, httpServletResponse);
        assertRequestResult(result, httpServletResponse);
    }

    private void assertRequestResult(String result, MockHttpServletResponse httpServletResponse) {
        if (HttpStatus.FOUND.value() == httpServletResponse.getStatus()) {
            String location = httpServletResponse.getHeader(HEADER_LOCATION_KEY);
            assertThat(location).isEqualTo(result);
            return;
        }

        assertThat(httpServletResponse.getForwardedUrl()).isEqualTo(result);
    }

    private static Stream<Arguments> provideRequest() {
        String getMethod = HttpMethod.GET.name();
        return Stream.of(
                Arguments.of(getMethod, "/", "home.jsp"),
                Arguments.of(getMethod, "/users", "/users/loginForm"),
                Arguments.of(getMethod, "/users/admin", "/user/profile.jsp")
        );
    }
}
