package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ForwardController {

    public ForwardController() {
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public String form(HttpServletRequest req, HttpServletResponse resp) {
        return "/user/form.jsp";
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public String loginForm(HttpServletRequest req, HttpServletResponse resp) {
        return "/user/login.jsp";
    }
}
