package next.controller;

import core.mvc.asis.DispatcherServlet;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.ServletException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ListUserControllerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() throws ServletException {
        request = new MockHttpServletRequest("GET", "/users");
        response = new MockHttpServletResponse();
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("ListUserController 테스트 - session 있는 경우 ok")
    @Test
    public void list_user_controller_test_with_session() throws ServletException, IOException {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User("Pooh", "honey", "Summer", "wow@wow"));
        request.setSession(session);
        dispatcherServlet.service(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("/user/list.jsp", response.getForwardedUrl());
    }

    @DisplayName("ListUserController 테스트 - session 없는 경우 redirect")
    @Test
    public void list_user_controller_test() throws ServletException, IOException {
        dispatcherServlet.service(request, response);

        assertEquals(HttpStatus.FOUND.value(), response.getStatus());
        assertEquals("/users/loginForm", response.getRedirectedUrl());
    }
}