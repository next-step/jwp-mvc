package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static core.annotation.web.RequestMethod.GET;
import static core.annotation.web.RequestMethod.POST;

@Controller
public class LoginController {

    @RequestMapping(value = "/users/login", method = POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User user = DataBase.findUserById(userId);
        if (canUserLogin(user, password)) {
            HttpSession session = request.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return new ModelAndView("redirect:/");
        }

        request.setAttribute("loginFailed", true);
        return new ModelAndView("/user/login.jsp");
    }

    @RequestMapping(value = "/users/loginForm", method = GET)
    public ModelAndView forwardLoginForm(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("/user/login.jsp");
    }

    private boolean canUserLogin(User user, String password) {
        return user != null && user.matchPassword(password);
    }
}
