package core.mvc.tobe.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @RequestMapping(value = "/users/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}
