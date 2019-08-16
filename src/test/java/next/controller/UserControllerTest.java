package next.controller;

import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.View;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        DataBase.deleteAll();
        userController = new UserController();
    }

    @DisplayName("로그인 유저 /users 요청시 유저 리스트 반환")
    @Test
    void loginedUserListTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User("loginedUser", "1234", "testUser", "test@abc.com"));
        request.setSession(session);
        ModelAndView mav = userController.users(request, new MockHttpServletResponse());

        assertThat(mav.getView().getViewName()).isEqualTo("/user/list");
    }

    @DisplayName("로그인 하지 않은 유저 /users 요청시 로그인폼 반환 검증")
    @Test
    void notLoginedUserListTest() {
        ModelAndView mav = userController.users(new MockHttpServletRequest(), new MockHttpServletResponse());

        View view = mav.getView();
        assertThat(view.getViewName()).isEqualTo("redirect:/users/loginForm");
    }

    @DisplayName("회원가입 폼")
    @Test
    void userRegisterForm() {
        ModelAndView mav = userController.userForm(new MockHttpServletRequest(), new MockHttpServletResponse());
        assertThat(mav.getView().getViewName()).isEqualTo("/user/form");
    }

    @DisplayName("로그인 폼")
    @Test
    void loginForm() {
        ModelAndView mav = userController.loginForm(new MockHttpServletRequest(), new MockHttpServletResponse());
        View view = mav.getView();
        assertThat(view.getViewName()).isEqualTo("/user/login");
    }

    @DisplayName("로그인 성공")
    @Test
    void login() {

        DataBase.addUser(new User("testUser", "1234", "testUser", "test@abc.com"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("userId", "testUser");
        request.setParameter("password", "1234");
        ModelAndView mav = userController.doLogin(request, new MockHttpServletResponse());

        View view = mav.getView();
        assertThat(view.getViewName()).isEqualTo("redirect:/");
    }

    @DisplayName("로그인 실패")
    @Test
    void loginFailed() {
        ModelAndView mav = userController.doLogin(new MockHttpServletRequest(), new MockHttpServletResponse());

        View view = mav.getView();
        Object loginFailed = mav.getObject("loginFailed");
        assertThat(view.getViewName()).isEqualTo("/user/login");
        assertThat(loginFailed).isEqualTo(true);
    }

    @DisplayName("회원 가입")
    @Test
    void register() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("userId", "testUser");
        request.setParameter("password", "1234");
        request.setParameter("name", "test");

        ModelAndView mav = userController.createUser(request, new MockHttpServletResponse());

        User user = DataBase.findUserById("testUser");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getName()).isEqualTo("test");

        View view = mav.getView();
        assertThat(view.getViewName()).isEqualTo("redirect:/");
    }

    @DisplayName("유저 프로필 조회")
    @Test
    void showProfile() {
        User user = new User("testUser", "1234", "test", "test@abc.com");
        DataBase.addUser(user);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("userId", "testUser");
        ModelAndView mav = userController.showProfile(request, new MockHttpServletResponse());
        View view = mav.getView();
        User findUser = (User) mav.getObject("user");
        assertThat(view.getViewName()).isEqualTo("/user/profile");
        assertThat(user).isEqualTo(findUser);
    }

    @DisplayName("유저 프로필 조회 실패")
    @Test
    void showProfileFail() {
        assertThatThrownBy(() -> userController.showProfile(new MockHttpServletRequest(), new MockHttpServletResponse()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @DisplayName("유저 수정 성공")
    @Test
    void update() {
        User user = new User("testUser", "1234", "test", "test@abc.com");
        DataBase.addUser(user);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setSession(session);
        request.setParameter("userId", user.getUserId());
        request.setParameter("name", "updatedName");
        request.setParameter("password", "5678");
        request.setParameter("email", "updateTest@abc.com");

        ModelAndView mav = userController.updateUser(request, new MockHttpServletResponse());
        View view = mav.getView();
        User findUser = DataBase.findUserById(user.getUserId());

        assertThat(view.getViewName()).isEqualTo("redirect:/");

        assertThat(findUser.getName()).isEqualTo("updatedName");
        assertThat(findUser.getPassword()).isEqualTo("5678");
        assertThat(findUser.getEmail()).isEqualTo("updateTest@abc.com");
    }

    @DisplayName("유저 수정 실패")
    @Test
    void updateFail() {
        assertThatThrownBy(() -> userController.updateUser(new MockHttpServletRequest(), new MockHttpServletResponse()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("다른 사용자의 정보를 수정할 수 없습니다.");
    }
}