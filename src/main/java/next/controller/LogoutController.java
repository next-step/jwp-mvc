package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;

@Controller
public class LogoutController {

    @RequestMapping("/users/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return ModelAndView.withJspView("redirect:/");
    }
}
