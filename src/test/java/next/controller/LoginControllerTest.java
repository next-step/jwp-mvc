package next.controller;

import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.ViewFactory;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerTest {

    private LoginController loginController;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        loginController = new LoginController();

        request = new MockHttpServletRequest("POST", "/users/login");
        response = new MockHttpServletResponse();
    }

    @DisplayName("로그인 실패 시 로그인 페이지로 이동한다.")
    @Test
    void loginFail() {
        // when
        final ModelAndView mav = loginController.execute(request, response);
        final View view = mav.getView();

        // then
        assertThat(view).isEqualTo(ViewFactory.create("/user/login.jsp"));

    }

    @DisplayName("로그인 성공 시 메인 페이지로 이동한다.")
    @Test
    void loginSuccess() {
        // given
        final String userId = "jaeyeonling";
        final String password = "password";

        DataBase.addUser(new User(userId, password, "Jaeyeon Kim", "jaeyeonling@gmail.com"));

        request.addParameter("userId", userId);
        request.addParameter("password", password);

        // when
        final ModelAndView mav = loginController.execute(request, response);
        final View view = mav.getView();

        // then
        assertThat(view).isEqualTo(ViewFactory.create("redirect:/"));
    }
}
