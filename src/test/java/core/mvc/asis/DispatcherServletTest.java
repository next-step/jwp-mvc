package core.mvc.asis;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DispatcherServletTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    public void setup() throws ServletException {
        response = new MockHttpServletResponse();
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

    }

    @DisplayName("HomeController 테스트")
    @Test
    public void home_controller_test() throws Exception {
        request = new MockHttpServletRequest("GET", "/");

        dispatcherServlet.service(request, response);

        assertEquals("home.jsp", response.getForwardedUrl());
    }

    @DisplayName("CreateUserController 테스트")
    @Test
    public void user_create_controller_test() throws ServletException, IOException {
        request = new MockHttpServletRequest("POST", "/users/create");
        User user = new User("admin", "password", "자바지기", "admin@slipp.net");
        request.addParameter("userId", user.getUserId());
        request.addParameter("password", user.getPassword());
        request.addParameter("name", user.getName());
        request.addParameter("email", user.getEmail());
        dispatcherServlet.service(request, response);

        assertEquals("/", response.getRedirectedUrl());
    }

    @DisplayName("ListUserController 테스트(session 없는 경우 redirect)")
    @Test
    public void list_user_controller_test() throws ServletException, IOException {
        request = new MockHttpServletRequest("GET", "/users");

        dispatcherServlet.service(request, response);

        assertEquals("/users/loginForm", response.getRedirectedUrl());
    }

    @DisplayName("LoginController 로그인 실패하는 경우  테스트(user가 없는 경우)")
    @Test
    public void login_controller_test_user_not_exist() throws ServletException, IOException {
        request = new MockHttpServletRequest("POST", "/users/login");

        request.setParameter("userId", "winter");
        request.setParameter("password", "fall");

        dispatcherServlet.service(request, response);

        assertEquals("/user/login.jsp", response.getForwardedUrl());
    }

    @DisplayName("LogoutController 테스트(legacy)")
    @Test
    public void logout_test() throws ServletException, IOException {
        request = new MockHttpServletRequest("GET", "/users/logout");

        dispatcherServlet.service(request, response);

        assertEquals("/", response.getRedirectedUrl());
    }
}