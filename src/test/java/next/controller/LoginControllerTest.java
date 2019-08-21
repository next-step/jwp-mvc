package next.controller;

import core.db.DataBase;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerTest {

    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        loginController = new LoginController();
    }

    @DisplayName("로그인 성공")
    @Test
    void login() throws Exception {

        DataBase.addUser(new User("testUser", "1234", "testUser", "test@abc.com"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("userId", "testUser");
        request.setParameter("password", "1234");
        String view = loginController.execute(request, response);

        assertThat(view).isEqualTo("redirect:/");
    }

    @DisplayName("로그인 실패")
    @Test
    void loginFailed() throws Exception {
        String view = loginController.execute(new MockHttpServletRequest(), new MockHttpServletResponse());

        assertThat(view).isEqualTo("/user/login.jsp");

    }
}