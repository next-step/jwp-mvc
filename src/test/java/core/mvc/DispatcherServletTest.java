package core.mvc;

import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.web.context.ApplicationContext;
import next.controller.UserSessionUtils;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static core.web.WebApplicationInitializer.DEFAULT_CONTROLLER_PACKAGE;
import static org.assertj.core.api.Assertions.assertThat;

class DispatcherServletTest extends AbstractDispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        DataBase.deleteAll();
        dispatcherServlet = new DispatcherServlet(new ApplicationContext(DEFAULT_CONTROLLER_PACKAGE));
        dispatcherServlet.init();
    }

    @DisplayName("메인 조회")
    @Test
    void home() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getForwardedUrl()).isEqualTo("home.jsp");
    }

    @DisplayName("회원가입")
    @Test
    void createUser() throws ServletException, IOException {
        User user = new User("testUserId", "1234", "testUser", "test@abc.com");
        MockHttpServletRequest request = userPostRequest("/users/create", RequestMethod.POST, user);

        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(302);

        User findUser = DataBase.findUserById("testUserId");
        assertThat(user).isEqualTo(findUser);
    }

    @DisplayName("유저 리스트 조회")
    @Test
    void showUserList() throws ServletException, IOException {

        User loginUser = new User("testUserId", "1234", "testUser", "test@abc.com");

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", loginUser);

        request.setSession(session);

        dispatcherServlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getForwardedUrl()).isEqualTo("/user/list.jsp");
    }

    @DisplayName("로그인")
    @Test
    void login() throws ServletException, IOException {
        User user = new User("testUserId", "1234", "testUser", "test@abc.com");

        MockHttpServletRequest request = userPostRequest("/users/create", RequestMethod.POST, user);
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        request = loginRequest(user);
        response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(302);
        assertThat(UserSessionUtils.getUserFromSession(request.getSession())).isEqualTo(user);

    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws ServletException, IOException {
        User user = new User("testUserId", "1234", "testUser", "test@abc.com");

        MockHttpServletRequest request = userPostRequest("/users/create", RequestMethod.POST, user);
        MockHttpServletResponse response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);

        request = loginRequest(user);
        response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        assertThat(UserSessionUtils.getUserFromSession(request.getSession())).isEqualTo(user);

        request = new MockHttpServletRequest("GET", "/users/logout");
        response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);

        assertThat(UserSessionUtils.getUserFromSession(request.getSession())).isNull();
    }

    @DisplayName("유저 업데이트")
    @Test
    void userUpdate() throws ServletException, IOException {
        User user = new User("testUserId", "1234", "testUser", "test@abc.com");

        MockHttpServletRequest request = userPostRequest("/users/create", RequestMethod.POST, user);
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        request = loginRequest(user);
        response = new MockHttpServletResponse();
        dispatcherServlet.service(request, response);
        HttpSession session = request.getSession();

        User updateUser = new User(user.getUserId(), "5678", "updatedName", "updatedEmail");
        request = userPostRequest("/users/update", RequestMethod.POST, updateUser);
        response = new MockHttpServletResponse();
        request.setSession(session);

        dispatcherServlet.service(request, response);

        assertThat(response.getStatus()).isEqualTo(302);

        User findUser = DataBase.findUserById(user.getUserId());

        assertThat(findUser).isEqualTo(updateUser);

    }
}