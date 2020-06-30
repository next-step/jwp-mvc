package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.view.JspView;
import core.mvc.view.RedirectView;
import core.mvc.view.View;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by iltaek on 2020/06/22 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
@Controller
public class ServletController {

    private static final String ILLEGAL_ATTEMPT = "다른 사용자의 정보를 수정할 수 없습니다.";
    private static final String CANNOT_FIND_USER = "사용자를 찾을 수 없습니다.";
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String HOME_CONTROLLER_VIEW = "home.jsp";
    private static final String FORM_FORWARD_VIEW = "/user/form.jsp";
    private static final String LOGIN_FORWARD_VIEW = "/user/login.jsp";
    private static final String USER_LIST_VIEW = "/user/list.jsp";
    private static final String REDIRECT_LOGIN_FORM = "redirect:/users/loginForm";
    private static final String REDIRECT_HOME = "redirect:/";
    private static final String PROFILE_VIEW = "/user/profile.jsp";
    private static final String UPDATE_USER_FORM_VIEW = "/user/updateForm.jsp";

    private static final Logger logger = LoggerFactory.getLogger(ServletController.class);

    @RequestMapping(value = "/")
    public ModelAndView home(HttpServletRequest request) {
        request.setAttribute("users", DataBase.findAll());
        return createModelAndViewWithViewName(HOME_CONTROLLER_VIEW);
    }

    @RequestMapping(value = "/index.html")
    public ModelAndView homeIndex(HttpServletRequest request) {
        request.setAttribute("users", DataBase.findAll());
        return createModelAndViewWithViewName(HOME_CONTROLLER_VIEW);
    }

    @RequestMapping(value = "/users/form")
    public ModelAndView forwardForm() {
        return createModelAndViewWithViewName(FORM_FORWARD_VIEW);
    }

    @RequestMapping(value = "/users/loginForm")
    public ModelAndView forwardLogin() {
        View view = getViewWithViewName(LOGIN_FORWARD_VIEW);
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/users")
    public ModelAndView listUser(HttpServletRequest request) {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return createModelAndViewWithViewName(REDIRECT_LOGIN_FORM);
        }

        request.setAttribute("users", DataBase.findAll());
        return createModelAndViewWithViewName(USER_LIST_VIEW);
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView loginUser(HttpServletRequest request, String userId, String password) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            request.setAttribute("loginFailed", true);
            return createModelAndViewWithViewName(LOGIN_FORWARD_VIEW);
        }
        if (user.matchPassword(password)) {
            HttpSession session = request.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return createModelAndViewWithViewName(REDIRECT_HOME);
        } else {
            request.setAttribute("loginFailed", true);
            return createModelAndViewWithViewName(LOGIN_FORWARD_VIEW);
        }
    }

    @RequestMapping(value = "/users/profile")
    public ModelAndView profileUser(HttpServletRequest request, String userId) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException(CANNOT_FIND_USER);
        }
        request.setAttribute("user", user);
        return createModelAndViewWithViewName(PROFILE_VIEW);
    }

    @RequestMapping(value = "/users/logout")
    public ModelAndView logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return createModelAndViewWithViewName(REDIRECT_HOME);
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView createUser(User user) {
        logger.debug("User : {}", user);

        DataBase.addUser(user);
        return createModelAndViewWithViewName(REDIRECT_HOME);
    }

    @RequestMapping(value = "/users/updateForm")
    public ModelAndView updateFormUser(HttpServletRequest request, String userId) {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException(ILLEGAL_ATTEMPT);
        }
        request.setAttribute("user", user);
        return createModelAndViewWithViewName(UPDATE_USER_FORM_VIEW);
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView updateUser(HttpServletRequest request, User updateUser) {
        User user = DataBase.findUserById(updateUser.getUserId());
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException(ILLEGAL_ATTEMPT);
        }

        logger.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return createModelAndViewWithViewName(REDIRECT_HOME);
    }

    private ModelAndView createModelAndViewWithViewName(String viewName) {
        View view = getViewWithViewName(viewName);
        return new ModelAndView(view);
    }

    private View getViewWithViewName(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new RedirectView(viewName);
        }
        return new JspView(viewName);
    }
}
