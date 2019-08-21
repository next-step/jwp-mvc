package next.controller;


import static org.assertj.core.api.Assertions.assertThat;

import core.db.DataBase;
import core.mvc.asis.DispatcherServlet;
import javax.servlet.http.HttpSession;
import next.model.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class LoginControllerTest {

    private LoginController loginController = new LoginController();

    @BeforeEach
    void setup() throws Exception {
        User user = new User("keeseung", "password", "이기승", "lee01494@gmail.com");

        DataBase.addUser(user);
    }

    @Test
    void login() {
        User user = DataBase.findUserById("keeseung");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("userId", "keeseung");
        request.setParameter("password", "password");
        loginController.login(request, response);

        HttpSession session = request.getSession();


        assertThat(session.getAttribute(UserSessionUtils.USER_SESSION_KEY)).isEqualTo(user);
    }

    @Test
    void loginFailure() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("userId", "keeseung");
        request.setParameter("password", "password111");
        loginController.login(request, response);


        assertThat(request.getAttribute("loginFailed")).isEqualTo(true);
    }
}