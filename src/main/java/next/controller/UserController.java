package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

@Controller("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping
    public ModelAndView getUsers(HttpSession session) throws Exception {
        if (!UserSessionUtils.isLogined(session)) {
            return new ModelAndView(new RedirectView("/users/loginForm"));
        }

        return new ModelAndView(new JspView("/user/list.jsp"));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createUser(User user) throws Exception {
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/profile")
    public ModelAndView getUserProfile(String userId) throws Exception {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        return new ModelAndView(new JspView("/user/profile.jsp"));
    }

    @RequestMapping(value = "/updateForm")
    public ModelAndView updateForm(String userId, HttpSession session) throws Exception {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        return new ModelAndView(new JspView("/user/updateForm.jsp"));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateUser(User updateUser, HttpSession session) throws Exception {
        User user = DataBase.findUserById(updateUser.getUserId());
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(String userId, String password, HttpSession session) throws Exception {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            return new ModelAndView(new JspView("/user/login.jsp"));
        }
        if (user.matchPassword(password)) {
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return new ModelAndView(new RedirectView("/"));
        } else {
            return new ModelAndView(new JspView("/user/login.jsp"));
        }
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpSession session) throws Exception {
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/form")
    public ModelAndView getForm() throws Exception {
        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    @RequestMapping(value = "/loginForm")
    public ModelAndView getLoginForm() throws Exception {
        return new ModelAndView(new JspView("/user/login.jsp"));
    }
}
