package next.controller.user;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.tobe.AbstractController;
import next.controller.UserSessionUtils;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @RequestMapping("/users/form")
    public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("/user/form.jsp");
    }

    @RequestMapping("/users/loginForm")
    public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("/user/login.jsp");
    }


    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        logger.debug("User : {}", user);

        DataBase.addUser(user);
        return jspView("redirect:/");
    }

    @RequestMapping("/users")
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        ModelAndView modelAndView = jspView("/user/list.jsp");
        modelAndView.addObject("users", DataBase.findAll());
        return modelAndView;
    }

    @RequestMapping("/users/profile")
    public ModelAndView profile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        ModelAndView modelAndView = jspView("/user/profile.jsp");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping("/users/updateForm")
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        ModelAndView modelAndView = jspView("/user/updateForm.jsp");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        logger.debug("Update User : {}", updateUser);
        user.update(updateUser);
        return jspView("redirect:/");
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            ModelAndView modelAndView = jspView("/user/login.jsp");
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return jspView("redirect:/");
        } else {
            ModelAndView modelAndView = jspView("/user/login.jsp");
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }
    }

    @RequestMapping("/users/logout")
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return jspView("redirect:/");
    }

}
