package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.tobe.ForwardView;
import core.mvc.tobe.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("home");
        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ForwardView("home.jsp"));
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users");
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return new ModelAndView(new RedirectView("/users/loginForm"));
        }

        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ForwardView("/user/list.jsp"));
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("login");
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User user = DataBase.findUserById(userId);

        if (user == null || !user.matchPassword(password)) {
            return loginFail(request);
        }

        return loginSuccess(request, user);

    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView userProfile(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("userProfile");
        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        request.setAttribute("user", user);
        return new ModelAndView(new ForwardView("/user/profile.jsp"));
    }

    @RequestMapping(value = "/users/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) {
        User user = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));

        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));
        user.update(updateUser);
        return new ModelAndView(new RedirectView("/"));
    }

    private ModelAndView loginSuccess(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
        return new ModelAndView(new RedirectView("/"));
    }

    private ModelAndView loginFail(HttpServletRequest request) {
        request.setAttribute("loginFailed", true);
        return new ModelAndView(new ForwardView("/user/login.jsp"));
    }

}
