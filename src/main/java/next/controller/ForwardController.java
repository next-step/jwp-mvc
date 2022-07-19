package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static core.annotation.web.RequestMethod.GET;

@Controller
public class ForwardController {

    @RequestMapping(value = "/users/form", method = GET)
    public String redirectUsersForm(HttpServletRequest req, HttpServletResponse resp) {
        return "/user/form.jsp";
    }

    @RequestMapping(value = "/users/loginForm", method = GET)
    public String redirectLoginForm(HttpServletRequest req, HttpServletResponse resp) {
        return "/user/login.jsp";
    }
}
