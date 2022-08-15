package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpSession;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView form() {
        return ModelAndView.jsp("/user/form.jsp");
    }

    @RequestMapping(value = "/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm() {
        return ModelAndView.jsp("/user/login.jsp");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView users(HttpSession session) {
        if (!UserSessionUtils.isLogined(session)) {
            return ModelAndView.redirect("/users/loginForm");
        }

        return ModelAndView.jsp("/user/list.jsp")
            .addObject("users", DataBase.findAll());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(String userId, String password, HttpSession session) {
        User user = DataBase.findUserById(userId);

        if (user == null) {
            return ModelAndView.jsp("/user/login.jsp")
                .addObject("loginFailed", true);
        }
        if (user.matchPassword(password)) {
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return ModelAndView.redirect("/");
        } else {
            return ModelAndView.jsp("/user/login.jsp")
                .addObject("loginFailed", true);
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile(String userId) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        return ModelAndView.jsp("/user/profile.jsp")
            .addObject("user", user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return ModelAndView.redirect("/");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(User user) {
        logger.debug("User : {}", user);

        DataBase.addUser(user);
        return ModelAndView.redirect("/");
    }

    @RequestMapping(value = "/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(String userId, HttpSession session) {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        return ModelAndView.jsp("/user/updateForm.jsp")
            .addObject("user", user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(String userId, User updateUser, HttpSession session) {
        User user = DataBase.findUserById(userId);

        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        logger.debug("Update User : {}", updateUser);

        user.update(updateUser);

        return ModelAndView.redirect("/");
    }


}
