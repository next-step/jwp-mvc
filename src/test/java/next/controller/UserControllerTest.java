package next.controller;

import core.db.DataBase;
import core.mvc.view.ModelAndView;
import core.mvc.view.View;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest {

    private UserController userController = new UserController();

    @BeforeEach
    void setUp() {
        DataBase.deleteAll();
    }

    @DisplayName("로그인 유저 /users 요청시 유저 리스트 반환")
    @Test
    void loginedUserListTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User("loginedUser", "1234", "testUser", "test@abc.com"));
        request.setSession(session);
        ModelAndView mav = userController.users(session);

        assertThat(mav.getView().getViewName()).isEqualTo("/user/list");
    }

    @DisplayName("로그인 하지 않은 유저 /users 요청시 로그인폼 반환 검증")
    @Test
    void notLoginedUserListTest() {
        ModelAndView mav = userController.users(new MockHttpSession());

        View view = mav.getView();
        assertThat(view.getViewName()).isEqualTo("redirect:/users/loginForm");
    }

    @DisplayName("회원가입 폼")
    @Test
    void userRegisterForm() {
        ModelAndView mav = userController.userForm();
        assertThat(mav.getView().getViewName()).isEqualTo("/user/form");
    }

    @DisplayName("로그인 폼")
    @Test
    void loginForm() {
        ModelAndView mav = userController.loginForm();
        View view = mav.getView();
        assertThat(view.getViewName()).isEqualTo("/user/login");
    }

    @DisplayName("회원 가입")
    @Test
    void register() {

        ModelAndView mav = userController.createUser("testUser", "1234", "test", "test@test.com");

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

        ModelAndView mav = userController.showProfile("testUser");
        View view = mav.getView();
        User findUser = (User) mav.getObject("user");
        assertThat(view.getViewName()).isEqualTo("/user/profile");
        assertThat(user).isEqualTo(findUser);
    }

    @DisplayName("유저 프로필 조회 실패")
    @Test
    void showProfileFail() {
        assertThatThrownBy(() -> userController.showProfile(null))
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

        ModelAndView mav = userController.updateUser(session, user.getUserId(), "5678", "updatedName", "updateTest@abc.com");
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
        assertThatThrownBy(() -> userController.updateUser(new MockHttpSession(), null, null, null, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("다른 사용자의 정보를 수정할 수 없습니다.");
    }
}