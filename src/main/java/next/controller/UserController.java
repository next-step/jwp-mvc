package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.ResultView;
import core.mvc.View;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users")
    public ModelAndView listUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return createModelAndView("redirect:/users/loginForm");
        }

        req.setAttribute("users", DataBase.findAll());
        return createModelAndView("/user/list.jsp");
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return createModelAndView("/user/login.jsp");
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView registForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return createModelAndView("/user/form.jsp");
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("User : {}", user);

        DataBase.addUser(user);
        View view = new ResultView("redirect:/");
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);

        if (user == null) {
            req.setAttribute("loginFailed", true);
            return createModelAndView("/user/login.jsp");
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return createModelAndView("redirect:/");
        }

        req.setAttribute("loginFailed", true);
        return createModelAndView("/user/login.jsp");
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView profile(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        req.setAttribute("user", user);
        return createModelAndView("/user/profile.jsp");
    }

    @RequestMapping(value = "/users/logout")
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return createModelAndView("redirect:/");
    }

    @RequestMapping(value = "/users/updateForm")
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        req.setAttribute("user", user);
        return createModelAndView("/user/updateForm.jsp");
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
        return createModelAndView("redirect:/");
    }

    private ModelAndView createModelAndView(String urlPath) {
        return new ModelAndView(new ResultView(urlPath));
    }
}
