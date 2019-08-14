package next.controller;

import core.db.DataBase;
import core.mvc.asis.DispatcherServlet;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerTest {

    private HttpServlet dispatcherServlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() throws ServletException {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        request = new MockHttpServletRequest("POST", "/users/login");
        response = new MockHttpServletResponse();
    }

    @DisplayName("로그인 실패 시 로그인 페이지로 이동한다.")
    @Test
    void loginFail() throws ServletException, IOException {
        // when
        dispatcherServlet.service(request, response);
        final String forwardUrl = response.getForwardedUrl();

        // then
        assertThat(forwardUrl).isEqualTo("/user/login.jsp");

    }

    @DisplayName("로그인 성공 시 메인 페이지로 이동한다.")
    @Test
    void loginSuccess() throws ServletException, IOException {
        // given
        final String userId = "jaeyeonling";
        final String password = "password";

        DataBase.addUser(new User(userId, password, "Jaeyeon Kim", "jaeyeonling@gmail.com"));

        request.addParameter("userId", userId);
        request.addParameter("password", password);

        // when
        dispatcherServlet.service(request, response);
        final String redirectUrl = response.getRedirectedUrl();

        // then
        assertThat(redirectUrl).isEqualTo("/");
    }
}
