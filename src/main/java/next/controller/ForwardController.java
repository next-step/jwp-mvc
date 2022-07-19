package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ForwardController {

    @RequestMapping("/users/form")
    public String redirectUsersForm(HttpServletRequest req, HttpServletResponse resp) {
        return "/user/form.jsp";
    }

    @RequestMapping("/users/loginForm")
    public String redirectLoginForm(HttpServletRequest req, HttpServletResponse resp) {
        return "/user/login.jsp";
    }
}
