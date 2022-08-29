package next.controller.tobe;

import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.ErrorView;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import next.controller.UserSessionUtils;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller(value = "/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "")
    public ModelAndView list(HttpServletRequest req) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView(new RedirectView("/users/loginForm"));
        }

        req.setAttribute("users", DataBase.findAll());

        return new ModelAndView(new JspView("/user/list.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("userId") String userId,
                              @RequestParam("password") String password,
                              HttpServletRequest req) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);

            return new ModelAndView(new JspView("/user/login.jsp"));
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);

            return new ModelAndView(new RedirectView("/"));
        } else {
            req.setAttribute("loginFailed", true);

            return new ModelAndView(new JspView("/user/login.jsp"));
        }
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/form")
    public ModelAndView form() {
        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    @RequestMapping(value = "/loginForm")
    public ModelAndView loginForm() {
        return new ModelAndView(new JspView("/user/login.jsp"));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody User user) {
        log.debug("User : {}", user);

        DataBase.addUser(user);

        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/updateForm")
    public ModelAndView updateForm(String userId, HttpServletRequest req) {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            return new ModelAndView(new ErrorView(403, "다른 사용자의 정보를 수정할 수 없습니다."));
        }
        req.setAttribute("user", user);

        return new ModelAndView(new JspView("/user/updateForm.jsp"));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(@RequestBody User updateUser, HttpServletRequest req) {
        User user = DataBase.findUserById(updateUser.getUserId());
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            return new ModelAndView(new ErrorView(403, "다른 사용자의 정보를 수정할 수 없습니다."));
        }

        log.debug("Update User : {}", updateUser);
        user.update(updateUser);

        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/profile/{userId}")
    public ModelAndView profile(@PathVariable("userId") String userId, HttpServletRequest req) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            return new ModelAndView(new ErrorView(404, "사용자를 찾을 수 없습니다."));
        }
        req.setAttribute("user", user);

        return new ModelAndView(new JspView("/user/profile.jsp"));
    }
}
