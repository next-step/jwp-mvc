package next.controller;

import static org.assertj.core.api.Assertions.assertThat;

import core.annotation.web.RequestMethod;
import core.mvc.asis.DispatcherServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HomeControllerTest {

    private HttpServlet dispatcherServlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() throws Exception {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        request = new MockHttpServletRequest("GET", "/");
        response = new MockHttpServletResponse();
    }

    @Test
    void home() throws ServletException, IOException {
        dispatcherServlet.service(request, response);
        String forwardedUrl = response.getForwardedUrl();

        assertThat(forwardedUrl).isEqualTo("home.jsp");
    }
}