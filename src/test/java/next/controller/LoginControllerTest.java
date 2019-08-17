package next.controller;

import core.mvc.tobe.view.JspView;
import core.mvc.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static next.controller.AnnotationController.execute;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LoginControllerTest {

    @DisplayName("로그인에 성공한다")
    @Test
    void login_success() {
        // given
        String userId = "admin";
        String password = "password";

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        // when
        execute(request, response);
        User user = (User) request.getSession().getAttribute(UserSessionUtils.USER_SESSION_KEY);

        // then
        assertThat(user).isNotNull();

    }

    @DisplayName("로그인에 실패한다")
    @ParameterizedTest
    @CsvSource({"'nonUser','password",
                "'admin','wrongPassword'"})
    void login_fail(String userId, String password) {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users/login");
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        // when
        ModelAndView result = execute(request);
        boolean isNotLogin = (boolean) result.getObject("loginFailed");

        // then
        assertThat(isNotLogin).isTrue();
        assertThat(result.getView()).isEqualTo(new JspView("/user/login.jsp"));
    }
}