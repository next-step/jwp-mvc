package core.mvc.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.controller.UserSessionUtils;

@Controller
public class LogoutController {

    @RequestMapping(value = "/users/logout")
    public ModelAndView logout(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }
}
