package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static core.annotation.web.RequestMethod.GET;

@Controller
public class LogoutController {
    @RequestMapping(value = "/users/logout", method = GET)
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return "redirect:/";
    }
}
