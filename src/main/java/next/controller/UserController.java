package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

/**
 * @author KingCjy
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping()
    public ModelAndView findUsers(HttpSession session) {
        if (!UserSessionUtils.isLogined(session)) {
            return new ModelAndView("redirect:/users/loginForm");
        }

        ModelAndView modelAndView = new ModelAndView("/user/list.jsp");
        modelAndView.addObject("users", DataBase.findAll());
        return modelAndView;
    }

    @RequestMapping("/create")
    public ModelAndView createUser(User user) {
        log.debug("User : {}", user);
        DataBase.addUser(user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/login")
    public ModelAndView login(HttpSession session, String userId, String password) {
        User user = DataBase.findUserById(userId);

        if (user == null) {
            ModelAndView modelAndView = new ModelAndView("/user/login.jsp");
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }

        if (user.matchPassword(password)) {
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return new ModelAndView("redirect:/");
        } else {
            ModelAndView modelAndView = new ModelAndView("/user/login.jsp");
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/profile")
    public ModelAndView getProfile(String userId) {
        User user = DataBase.findUserById(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        ModelAndView modelAndView = new ModelAndView("/user/profile.jsp");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping("/updateForm")
    public ModelAndView updateForm(HttpSession session, String userId) {
        User user = DataBase.findUserById(userId);

        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        ModelAndView modelAndView = new ModelAndView("/user/updateForm.jsp");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping("/update")
    public ModelAndView update(User updateUser, String userId, HttpSession session) {
        User user = DataBase.findUserById(userId);

        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return new ModelAndView("redirect:/");
    }

//    @RequestMapping("/form")
//    public ModelAndView form() {
//        return new ModelAndView("/user/form.jsp");
//    }
//
//    @RequestMapping("/loginForm")
//    public ModelAndView loginForm() {
//        return new ModelAndView("/user/login.jsp");
//    }
}
