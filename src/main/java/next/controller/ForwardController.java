package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.view.JspView;
import core.mvc.view.ModelAndView;

@Controller
public class ForwardController {

    @RequestMapping("/users/form")
    public ModelAndView redirectUsersForm() {
        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    @RequestMapping("/users/loginForm")
    public ModelAndView redirectLoginForm() {
        return new ModelAndView(new JspView("/user/login.jsp"));
    }
}
