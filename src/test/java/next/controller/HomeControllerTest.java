package next.controller;

import core.mvc.asis.DispatcherServlet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    @DisplayName("HomeController 테스트")
    @Test
    public void home_controller_test() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        MockHttpServletResponse response = new MockHttpServletResponse();
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        dispatcherServlet.service(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("home.jsp", response.getForwardedUrl());
    }
}