package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static core.annotation.web.RequestMethod.GET;
import static core.annotation.web.RequestMethod.POST;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users/create", method = POST)
    public String createUser(User user) {
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return "redirect:/";
    }

    @RequestMapping("/users")
    public String getUsers(HttpServletRequest req) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return "redirect:/users/loginForm";
        }

        req.setAttribute("users", DataBase.findAll());
        return "/user/list.jsp";
    }
    @RequestMapping(value = "/users/profile", method = GET)
    public String profileInfo(HttpServletRequest req) {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        req.setAttribute("user", user);
        return "/user/profile.jsp";
    }

    @RequestMapping(value = "/users/loginForm", method = GET)
    public String loginForm() {
        return "/user/login.jsp";
    }

    @RequestMapping(value = "/users/login", method = POST)
    public String login(@RequestParam String userId, @RequestParam("password") String pw, HttpServletRequest req) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
        if (user.matchPassword(pw)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        } else {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
    }

    @RequestMapping(value = "/users/logout", method = GET)
    public String logout(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return "redirect:/";
    }

    @RequestMapping(value = "/users/form", method = GET)
    public String joinForm() {
        return "/user/form.jsp";
    }

    @RequestMapping(value = "/users/updateForm", method = GET)
    public String userUpdateForm(String userId, HttpServletRequest req) {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        req.setAttribute("user", user);
        return "/user/updateForm.jsp";
    }

    @RequestMapping(value = "/users/update", method = POST)
    public String updateUserInfo(HttpServletRequest req, User updateRequestUser) {
        User user = DataBase.findUserById(updateRequestUser.getUserId());
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        log.debug("Update User : {}", updateRequestUser);
        user.update(updateRequestUser);
        return "redirect:/";
    }
}
