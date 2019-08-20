package core.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class DispatcherServletTest {

    private HttpServlet dispatcherServlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() throws Exception {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void home() throws ServletException, IOException {
        request.setMethod("GET");
        request.setRequestURI("/");

        dispatcherServlet.service(request, response);
        String forwardedUrl = response.getForwardedUrl();

        assertThat(forwardedUrl).isEqualTo("home.jsp");
    }
}