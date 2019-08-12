package next.controller;

import core.db.DataBase;
import core.mvc.asis.DispatcherServlet;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginControllerTest {
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() throws ServletException {
        request = new MockHttpServletRequest("POST", "/users/login");
        response = new MockHttpServletResponse();
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @DisplayName("LoginController 로그인 성공하는 경우 테스트")
    @Test
    public void login_controller_test_login_success() throws ServletException, IOException {
        User user = new User("Pooh", "honey", "Summer", "wow@wow");
        DataBase.addUser(user);

        request.setParameter("userId", "Pooh");
        request.setParameter("password", "honey");

        dispatcherServlet.service(request, response);

        assertEquals(HttpStatus.FOUND.value(), response.getStatus());
        assertEquals("/", response.getRedirectedUrl());
    }

    @DisplayName("LoginController 로그인 실패하는 경우  테스트 : user가 없는 경우")
    @Test
    public void login_controller_test_user_not_exist() throws ServletException, IOException {
        request.setParameter("userId", "winter");
        request.setParameter("password", "fall");

        dispatcherServlet.service(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("/user/login.jsp", response.getForwardedUrl());
    }

    @DisplayName("LoginController 로그인 실패하는 경우 테스트 : 비밀번호가 다른 경우")
    @Test
    public void login_controller_test_not_match_password() throws ServletException, IOException {
        User user = new User("Jihan", "wow", "test", "test@test");
        DataBase.addUser(user);

        request.setParameter("userId", "Jihan");
        request.setParameter("password", "wowwow");

        dispatcherServlet.service(request, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("/user/login.jsp", response.getForwardedUrl());
    }
}