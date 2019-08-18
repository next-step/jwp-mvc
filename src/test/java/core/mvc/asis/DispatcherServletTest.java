package core.mvc.asis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    @DisplayName("Request is handled by annotation mapping")
    void serviceToAnnotationMapping() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getForwardedUrl()).isNotBlank();
        assertThat(response.getRedirectedUrl()).isNull();
    }

    @Test
    @DisplayName("Request is handled by legacy request mapping")
    void serviceToLegacyRequestMapping() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getForwardedUrl()).isNotBlank();
        assertThat(response.getRedirectedUrl()).isNull();
    }
}