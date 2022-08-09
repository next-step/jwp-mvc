package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller(path = "/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest request) {
        if (!UserSessionUtils.isLogined(request.getSession())) {

            return ModelAndView.newInstance("redirect:/users/loginForm");
        }

        request.setAttribute("users", DataBase.findAll());
        return ModelAndView.newInstance("/user/list.jsp");
    }

    @RequestMapping("/form")
    public ModelAndView userForm() {
        return ModelAndView.newInstance("/user/form.jsp");
    }

    @RequestMapping("/loginForm")
    public ModelAndView userLoginForm() {
        return ModelAndView.newInstance("/user/login.jsp");
    }

    @RequestMapping("/login")
    public ModelAndView login(String userId, String password, HttpSession session,  HttpServletRequest request) {
        User user = DataBase.findUserById(userId);

        if (user == null) {
            request.setAttribute("loginFailed", true);
            return ModelAndView.newInstance("/user/login.jsp");
        }
        if (user.matchPassword(password)) {
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return ModelAndView.newInstance("redirect:/");
        } else {
            request.setAttribute("loginFailed", true);
            return ModelAndView.newInstance("/user/login.jsp");
        }
    }

    @RequestMapping("/profile")
    public ModelAndView profile(String userId, HttpServletRequest request) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        request.setAttribute("user", user);
        return ModelAndView.newInstance("/user/profile.jsp");
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return ModelAndView.newInstance("redirect:/");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createUser(User user) {
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return ModelAndView.newInstance("redirect:/");
    }

    @RequestMapping(value = "/updateForm", method = RequestMethod.GET)
    public ModelAndView updateUserForm(String userId, HttpSession session, HttpServletRequest request) {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        request.setAttribute("user", user);
        return ModelAndView.newInstance("/user/updateForm.jsp");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateUser(User updateUser, HttpSession session , HttpServletRequest request) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return ModelAndView.newInstance("redirect:/");
    }
}
