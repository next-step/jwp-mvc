package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.view.ModelAndView;
import core.mvc.view.ResourceView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/create")
    public ModelAndView createUser(User user) {
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return new ModelAndView(new ResourceView("redirect:/"));
    }

    @RequestMapping
    public ModelAndView showUsers(HttpServletRequest req) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView(new ResourceView("redirect:/users/loginForm"));
        }

        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ResourceView("/user/list.jsp"));
    }

    @RequestMapping("/login")
    public ModelAndView userLogin(HttpServletRequest req, String userId, String password) {
        User user = DataBase.findUserById(userId);
        if (isLoginFail(password, user)) {
            req.setAttribute("loginFailed", true);
            return new ModelAndView(new ResourceView("/user/login.jsp"));
        }
        HttpSession session = req.getSession();
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
        return new ModelAndView(new ResourceView("redirect:/"));
    }

    private static boolean isLoginFail(String password, User user) {
        return user == null || !user.matchPassword(password);
    }

    @RequestMapping("/logout")
    public ModelAndView userLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new ResourceView("redirect:/"));
    }

    @RequestMapping("/profile")
    public ModelAndView showUserProfile(HttpServletRequest request, String userId) {
        User user = Optional.ofNullable(DataBase.findUserById(userId)).orElseThrow(() -> new NullPointerException("사용자를 찾을 수 없습니다."));
        request.setAttribute("user", user);
        return new ModelAndView(new ResourceView("/user/profile.jsp"));
    }

    @RequestMapping("/updateForm")
    public ModelAndView showUserUpdateForm(HttpServletRequest request, String userId) {
        User user = DataBase.findUserById(userId);
        validateSameUser(request, user);
        request.setAttribute("user", user);
        return new ModelAndView(new ResourceView("/user/updateForm.jsp"));
    }

    @RequestMapping("/update")
    public ModelAndView userUpdate(HttpServletRequest request, String userId, User updateUser) {
        User user = DataBase.findUserById(userId);
        validateSameUser(request, user);

        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return new ModelAndView(new ResourceView("redirect:/"));
    }

    private static void validateSameUser(HttpServletRequest request, User user) {
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
    }
}
