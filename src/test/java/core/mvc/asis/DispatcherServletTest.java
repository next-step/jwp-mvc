package core.mvc.asis;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.ServletException;
import java.io.IOException;

@DisplayName("디스패처 서블릿")
class DispatcherServletTest {
    private final static DispatcherServlet dispatcher = new DispatcherServlet();

    @BeforeAll
    static void init() throws ServletException {
        dispatcher.init();
    }

    @Test
    @DisplayName("서비스 결과는?")
    void serviceGet() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/profile");
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setParameter("userId", "admin");

        dispatcher.service(request, response);

        System.out.println(response.getForwardedUrl());
        System.out.println(request.getAttribute("user"));
        System.out.println(response.getStatus());
        System.out.println(response.getContentLength());
    }

    @Test
    @DisplayName("redirect 서비스 결과는?")
    void serviceRedirect() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/logout");
        MockHttpServletResponse response = new MockHttpServletResponse();


        dispatcher.service(request, response);

        System.out.println(response.getForwardedUrl());
        System.out.println(response.getRedirectedUrl());
        System.out.println(response.getStatus());
        System.out.println(response.getContentLength());
    }
}
