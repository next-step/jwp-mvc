package next.controller;

import core.mvc.asis.DispatcherServlet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LogoutControllerTest {
    @DisplayName("LogoutController 테스트 - legacy")
    @Test
    public void logout_test() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/logout");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        dispatcherServlet.service(request, response);

        assertEquals(HttpStatus.FOUND.value(), response.getStatus());
        assertEquals("/", response.getRedirectedUrl());
    }
}