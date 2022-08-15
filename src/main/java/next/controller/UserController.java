package next.controller;

import static core.annotation.web.RequestMethod.GET;
import static core.annotation.web.RequestMethod.POST;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;

@Controller("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/form", method = GET)
    public ModelAndView createForm() {
        logging("create user form");

        return createModelAndView("/user/form.jsp");
    }

    @RequestMapping(value = "/create", method = POST)
    public ModelAndView create(String userId, String password, String name, String email) {
        logging("create user");

        User user = new User(userId, password, name, email);
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return createModelAndView("redirect:/");
    }

    @RequestMapping(method = GET)
    public ModelAndView list(HttpServletRequest req) {
        logging("user list");

        if (!UserSessionUtils.isLogined(req.getSession())) {
            return createModelAndView("redirect:/users/loginForm");
        }

        req.setAttribute("users", DataBase.findAll());
        return createModelAndView("/user/list.jsp");
    }

    @RequestMapping(value = "/loginForm", method = GET)
    public ModelAndView loginForm() {
        logging("login form");

        return createModelAndView("/user/login.jsp");
    }

    @RequestMapping(value = "/login", method = POST)
    public ModelAndView login(String userId, String password) {
        logging("login");

        User user = DataBase.findUserById(userId);

        if (user == null) {
            return createModelAndView("/user/login.jsp", Map.of("loginFailed", true));
        }
        if (user.matchPassword(password)) {
            return createModelAndView("redirect:/", Map.of(UserSessionUtils.USER_SESSION_KEY, user));
        }
        return createModelAndView("/user/login.jsp", Map.of("loginFailed", true));
    }

    @RequestMapping(value = "/logout", method = GET)
    public ModelAndView logout() {
        logging("logout");

        return createModelAndView("redirect:/", Map.of(UserSessionUtils.USER_DELETE_KEY, true));
    }

    @RequestMapping(value = "/profile", method = GET)
    public ModelAndView profile(HttpServletRequest req, HttpServletResponse resp) {
        logging("profile");

        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        req.setAttribute("user", user);
        return createModelAndView("/user/profile.jsp");
    }

    @RequestMapping(value = "/updateForm", method = GET)
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) {
        logging("update form");

        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        req.setAttribute("user", user);
        return createModelAndView("/user/updateForm.jsp");
    }

    @RequestMapping(value = "/update", method = POST)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) {
        logging("update");

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

    private ModelAndView createModelAndView(String path) {
        return new ModelAndView(new JspView(path));
    }

    private ModelAndView createModelAndView(String path, Map<String, Object> attributes) {
        ModelAndView modelAndView = new ModelAndView(new JspView(path));
        attributes.forEach(modelAndView::addObject);
        return modelAndView;
    }

    private void logging(String requestName) {
        log.info("Annotation Mapping Handler {} Complete", requestName);
    }
}
