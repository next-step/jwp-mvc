package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller(value = "/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView usersForm(HttpServletRequest req, HttpServletResponse resp) {
        JspView jspView = new JspView("/user/form.jsp");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/loginForm", method = RequestMethod.GET)
    public ModelAndView usersLoginForm(HttpServletRequest req, HttpServletResponse resp) {
        JspView jspView = new JspView("/user/login.jsp");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest req, HttpServletResponse resp) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            JspView jspView = new JspView("redirect:/users/loginForm");
            return new ModelAndView(jspView);
        }

        req.setAttribute("users", DataBase.findAll());
        JspView jspView = new JspView("/user/list.jsp");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginUser(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);
            JspView jspView = new JspView("/user/login.jsp");
            return new ModelAndView(jspView);
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            JspView jspView = new JspView("redirect:/");
            return new ModelAndView(jspView);
        } else {
            req.setAttribute("loginFailed", true);
            JspView jspView = new JspView("/user/login.jsp");
            return new ModelAndView(jspView);
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView getProfile(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        req.setAttribute("user", user);
        JspView jspView = new JspView("/user/profile.jsp");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logoutUser(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        JspView jspView = new JspView("redirect:/");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
            req.getParameter("email"));
        logger.debug("User : {}", user);

        DataBase.addUser(user);
        JspView jspView = new JspView("redirect:/");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/updateForm", method = RequestMethod.GET)
    public ModelAndView getUpdateUserForm(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        req.setAttribute("user", user);
        JspView jspView = new JspView("/user/updateForm.jsp");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ModelAndView updateUser(HttpServletRequest req, HttpServletResponse resp) {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
            req.getParameter("email"));
        logger.debug("Update User : {}", updateUser);
        user.update(updateUser);
        JspView jspView = new JspView("redirect:/");
        return new ModelAndView(jspView);
    }

}
