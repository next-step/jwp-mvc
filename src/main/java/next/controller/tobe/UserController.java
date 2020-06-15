package next.controller.tobe;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import next.controller.asis.UserSessionUtils;
import next.model.User;
import next.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static core.annotation.web.RequestMethod.GET;
import static core.annotation.web.RequestMethod.POST;

@Controller
@RequestMapping(value = "/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/create", method = POST)
    public String create(User user) {
        log.debug("User : {}", StringUtils.toPrettyJson(user));

        DataBase.addUser(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/update", method = POST)
    public String update(HttpServletRequest req, String userId, User updateUser) throws Exception {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), updateUser)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return "redirect:/";
    }


    @RequestMapping(value = "/update/form", method = POST)
    public String updateForm(HttpServletRequest req, String userId) throws Exception {
        log.debug("userId: {}", userId);

        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        req.setAttribute("user", user);
        return "/user/updateForm.jsp";
    }

    @RequestMapping(value = "/login", method = POST)
    public String login(HttpServletRequest req, String userId, String password) throws Exception {
        log.debug("userId: {}", userId);

        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        }
        else {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
    }

    @RequestMapping(method = GET)
    public String list(HttpServletRequest req) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return "redirect:/users/loginForm";
        }

        req.setAttribute("users", DataBase.findAll());
        return "/user/list.jsp";
    }

    @RequestMapping(value = "/logout", method = GET)
    public String logout(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return "redirect:/";
    }

    @RequestMapping(value = "/profile/{id}", method = GET)
    public String profile(
        HttpServletRequest req,
        @PathVariable(name = "id") String userId
    ) {
        log.debug("userId: {}", userId);

        User user = DataBase.findUserById(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        req.setAttribute("user", user);

        return "/user/profile.jsp";
    }
}
