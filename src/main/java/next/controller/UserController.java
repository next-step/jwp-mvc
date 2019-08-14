package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("User : {}", user);

        DataBase.addUser(user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView listUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView("redirect:/users/loginForm");
        }

        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView("/user/list.jsp");
    }

    @RequestMapping("/users/profile")
    public ModelAndView profileUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        req.setAttribute("user", user);
        return new ModelAndView("/user/profile.jsp");
    }

}
