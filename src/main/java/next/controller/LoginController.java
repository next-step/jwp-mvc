package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(String userId, String password, HttpServletRequest request) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            request.setAttribute("loginFailed", true);
            return ModelAndView.fromJspView("/user/login.jsp");
        }
        if (user.matchPassword(password)) {
            HttpSession session = request.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return ModelAndView.fromJspView("redirect:/");
        } else {
            request.setAttribute("loginFailed", true);
            return ModelAndView.fromJspView("/user/login.jsp");
        }
    }
}
