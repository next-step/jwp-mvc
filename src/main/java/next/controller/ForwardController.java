package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.view.ResourceView;
import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ForwardController {
    @RequestMapping("/users/form")
    public ModelAndView showUsersForm(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new ResourceView("/user/form.jsp"));
    }

    @RequestMapping("/users/loginForm")
    public ModelAndView showLoginForm(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new ResourceView("/user/login.jsp"));
    }
}
