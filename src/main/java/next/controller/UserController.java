package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ForwardView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/users")
    public ModelAndView userList(HttpServletRequest req, HttpServletResponse resp) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return ModelAndView.from(RedirectView.from("/users/loginForm"));
        }
        return ModelAndView.of(Map.of("users", DataBase.findAll()), ForwardView.from("/user/list.jsp"));
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return ModelAndView.from(RedirectView.root());
    }

    @RequestMapping(value = "/users/form")
    public ModelAndView userForm(HttpServletRequest req, HttpServletResponse resp) {
        return ModelAndView.from(ForwardView.from("/user/form.jsp"));
    }

    @RequestMapping("/users/profile")
    public ModelAndView profile(HttpServletRequest req, HttpServletResponse resp) {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        return ModelAndView.of(Map.of("user", user), ForwardView.from("/user/profile.jsp"));
    }

    @RequestMapping(value = "/users/loginForm")
    public ModelAndView userLoginForm(HttpServletRequest req, HttpServletResponse resp) {
        return ModelAndView.from(ForwardView.from("/user/login.jsp"));
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse resp) {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (user == null) {
            return ModelAndView.of(Map.of("loginFailed", true),
                    ForwardView.from("/user/login.jsp"));
        }
        String password = req.getParameter("password");
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return ModelAndView.from(RedirectView.root());
        }
        return ModelAndView.of(Map.of("loginFailed", true),
                ForwardView.from("/user/login.jsp"));
    }

    @RequestMapping(value = "/users/logout")
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return ModelAndView.from(RedirectView.root());
    }

    @RequestMapping(value = "/users/updateForm")
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        return ModelAndView.of(Map.of("user", user),
                ForwardView.from("/user/updateForm.jsp"));
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return ModelAndView.from(RedirectView.root());
    }
}
