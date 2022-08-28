package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.view.ModelAndView;
import core.mvc.view.ResourceView;

@Controller
public class ForwardController {
    @RequestMapping("/users/form")
    public ModelAndView showUsersForm() {
        return new ModelAndView(new ResourceView("/user/form.jsp"));
    }

    @RequestMapping("/users/loginForm")
    public ModelAndView showLoginForm() {
        return new ModelAndView(new ResourceView("/user/login.jsp"));
    }
}
