package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.view.JspView;
import core.mvc.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    @RequestMapping("/users/login")
    public ModelAndView login(HttpServletRequest request, @RequestParam String userId, @RequestParam String password) {

        final User user = DataBase.findUserById(userId);

        if (user == null) {
            request.setAttribute("loginFailed", true);
            return new ModelAndView(new JspView("/user/login.jsp"));
        }

        if (!user.matchPassword(password)) {
            request.setAttribute("loginFailed", true);
            return new ModelAndView(new JspView("/user/login.jsp"));
        }

        final HttpSession session = request.getSession();
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping("/users/logout")
    public ModelAndView logout(HttpServletRequest request) {
        final HttpSession session = request.getSession();

        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }

}
