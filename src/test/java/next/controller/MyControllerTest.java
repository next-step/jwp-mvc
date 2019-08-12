package next.controller;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.asis.DispatcherServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class MyControllerTest {

    private HttpServlet dispatcherServlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() throws Exception {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        response = new MockHttpServletResponse();

    }

    @Test
    void list() throws ServletException, IOException {
        request = new MockHttpServletRequest("GET", "/users");

        dispatcherServlet.service(request, response);

        String forwardedUrl = response.getForwardedUrl();
        assertThat(forwardedUrl).isEqualTo("/users/list.jsp");
    }

    @Test
    void show() throws ServletException, IOException {
        request = new MockHttpServletRequest("GET", "/users/show");

        dispatcherServlet.service(request, response);

        String forwardedUrl = response.getForwardedUrl();
        assertThat(forwardedUrl).isEqualTo("/users/show.jsp");
    }

    @Test
    void create() throws ServletException, IOException {
        request = new MockHttpServletRequest("POST", "/users");

        dispatcherServlet.service(request, response);

        String redirectedUrl = response.getRedirectedUrl();
        assertThat(redirectedUrl).isEqualTo("/users");
    }
}