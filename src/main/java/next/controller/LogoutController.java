package next.controller;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@core.annotation.web.Controller
public class LogoutController implements Controller {

    @Override
    @RequestMapping(value = "/users/logout", method = RequestMethod.POST)
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return "redirect:/";
    }
}
