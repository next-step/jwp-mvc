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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest req, HttpServletResponse resp) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            RedirectView redirectView = new RedirectView("redirect:/users/loginForm");
            return new ModelAndView(redirectView);
        }

        JspView jspView = new JspView("/user/list");
        ModelAndView mav = new ModelAndView(jspView);
        mav.addObject("users", DataBase.findAll());
        return mav;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView userForm(HttpServletRequest req, HttpServletResponse resp) {
        return new ModelAndView(new JspView("/user/form"));
    }

    @RequestMapping(value = "/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) {
        return new ModelAndView(new JspView("/user/login"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView doLogin(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);

        ModelAndView mav = new ModelAndView();

        if (user == null) {
            mav.addView(new JspView("/user/login"));
            mav.addObject("loginFailed", true);
            return mav;
        }

        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            mav.addView(new RedirectView("redirect:/"));
            return mav;
        }

        mav.addView(new JspView("/user/login"));
        mav.addObject("loginFailed", true);
        return mav;

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new RedirectView("redirect:/"));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp) {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("redirect:/"));
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView showProfile(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);

        ModelAndView mav = new ModelAndView();

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        mav.addView(new JspView("/user/profile"));
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        return new ModelAndView(new JspView("/user/updateForm")).addObject("user", user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateUser(HttpServletRequest req, HttpServletResponse resp) {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("Update User : {}", updateUser);

        user.update(updateUser);

        return new ModelAndView(new RedirectView("redirect:/"));
    }
}
